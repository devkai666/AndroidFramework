package devkai.app.base

import java.util.HashMap

/**
 * Created by xingkai on 2017/4/5.
 */

class UrlConfig private constructor() {

    //url keys
    val COMPANY_LIST_URL = "COMPANY_LIST_URL"

    val configMap = HashMap<String, BaseUrl>()

    init {
        configMap.put(COMPANY_LIST_URL, BaseUrl("company", DEFAULT_TIME))
    }

    companion object {
        //默认缓存时间1小时
        private val DEFAULT_TIME = ACache.TIME_HOUR
        @get:Synchronized
        var instance: UrlConfig? = null
            private set
    }
}
