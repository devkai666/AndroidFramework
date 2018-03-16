package devkai.app.base

/**
 * Created by xingkai on 2017/4/1.
 */

interface IHandler<in T> {
    fun onFinish(result: T?, error: Throwable?)
}
