package devkai.app.base

import android.content.Context
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import devkai.app.common.Consts
import devkai.app.main.MyApplication
import devkai.app.utils.Utils
import roboguice.util.Ln

/**
 * Created by Xingkai on 15/10/30.
 * Rest Api 请求类
 */
object RestClient {

    private val mCache = ACache.get(MyApplication.instance?.applicationContext)
    private val gson = GsonProvider.instance?.get()

    val api: String
        external get

    init {
        System.loadLibrary("keypipe")
    }

    private operator fun get(client: AsyncHttpClient, url: String?, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler)
    }

    private fun post(client: AsyncHttpClient, url: String?, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler)
    }

    fun <K, V> getData(context: Context, loadType: Int, httpType: String, urlKey: String, params: RequestParams, cls: Class<K>, sonCls: Class<V>, handle: IHandler<K>) {
        val client = AsyncHttpClient(true, 80, 443)
        client.addHeader("Accept", Utils.getAcceptHeader("1.0"))
        getData(client, context, loadType, httpType, urlKey, params, cls, sonCls, handle)
    }

    /**
     * 获取请求数据
     *
     * @param loadType 加载类型 0 自动加载，1 刷新
     * @param httpType 请求类型GET OR POST
     * @param urlKey   请求地址key
     * @param params   传入参数
     * @param handle   JSON处理类
     * @param <K>      返回Model
    </K> */
    fun <K, V> getData(client: AsyncHttpClient, context: Context, loadType: Int, httpType: String, urlKey: String, params: RequestParams, cls: Class<K>, sonCls: Class<V>, handle: IHandler<K>) {
        setHeaders(client)

        val baseUrl = UrlConfig.instance?.configMap?.get(urlKey)
        val key = AsyncHttpClient.getUrlWithQueryString(true, getAbsoluteUrl(baseUrl?.url), params)

        //有缓存
        if (loadType == 0 && Utils.isNotEmpty(getCacheData(key))) {
            Ln.i(key + " cache")
            var responseStr = getCacheData(key)
            val simpleResult = gson?.fromJson(responseStr, SimpleResult::class.java)
            Ln.i("status--->" + simpleResult?.status)
            Ln.i("msg--->" + simpleResult?.msg!!)
            try {
                if (simpleResult.status == 0) {
                    setCacheData(key, responseStr, baseUrl?.time)
                } else {
                    if (simpleResult.status < 0) {
                        Utils.showError(context, simpleResult.msg!!)
                    }
                    simpleResult.data = sonCls.newInstance()
                    responseStr = gson?.toJson(simpleResult)
                }
                val result = gson?.fromJson(responseStr, cls)
                handle.onFinish(result, null!!)
            } catch (ex: Exception) {
                handle.onFinish(null, Throwable(simpleResult.msg))
            }

        } else if (loadType == 1 || !Utils.isNotEmpty(getCacheData(key))) {
            Ln.i(key + " server")
            when (httpType) {
                Consts.KEY_GET -> get(client, baseUrl?.url, params, object : AsyncHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                        var responseStr = String(responseBody)
                        val simpleResult = gson?.fromJson(responseStr, SimpleResult::class.java)
                        Ln.i("status--->" + simpleResult?.status)
                        Ln.i("msg--->" + simpleResult?.msg)
                        try {
                            if (simpleResult?.status == 0) {
                                setCacheData(key, responseStr, baseUrl?.time)
                            } else {
                                if (simpleResult?.status!! < 0) {
                                    Utils.showError(context, simpleResult.msg!!)
                                }
                                simpleResult.data = sonCls.newInstance()
                                responseStr = gson?.toJson(simpleResult)!!
                            }
                            val result = gson?.fromJson(responseStr, cls)
                            handle.onFinish(result, null)
                        } catch (ex: Exception) {
                            handle.onFinish(null, Throwable(simpleResult?.msg))
                        }

                    }

                    override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                        handle.onFinish(null, error)
                    }
                })
                Consts.KEY_POST -> post(client, baseUrl?.url, params, object : AsyncHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                        var responseStr = String(responseBody)
                        val simpleResult = gson?.fromJson(responseStr, SimpleResult::class.java)
                        Ln.i("status--->" + simpleResult?.status)
                        Ln.i("msg--->" + simpleResult?.msg!!)
                        try {
                            if (simpleResult.status == 0) {
                                setCacheData(key, responseStr, baseUrl?.time)
                            } else {
                                if (simpleResult.status < 0) {
                                    Utils.showError(context, simpleResult.msg!!)
                                }
                                simpleResult.data = sonCls.newInstance()
                                responseStr = gson?.toJson(simpleResult)!!
                            }
                            val result = gson?.fromJson(responseStr, cls)
                            handle.onFinish(result, null!!)
                        } catch (ex: Exception) {
                            handle.onFinish(null, Throwable(simpleResult.msg))
                        }

                    }

                    override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                        handle.onFinish(null, error)
                    }
                })
            }
        }
    }

    private fun getCacheData(key: String): String? {
        return mCache.getAsString(key)
    }

    private fun setCacheData(key: String, value: String?, time: Int?) {
        mCache.put(key, value, time!!)
    }

    fun getAbsoluteUrl(relativeUrl: String?): String {
        return api + "api/" + relativeUrl
    }

    private fun setHeaders(client: AsyncHttpClient) {
        //add headers
    }
}
