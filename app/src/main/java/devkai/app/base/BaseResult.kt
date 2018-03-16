package devkai.app.base


import java.io.Serializable

import devkai.app.utils.Utils

/**
 * Created by xingkai on 2017/4/7.
 */
open class BaseResult : Serializable {
    var status: Int = 0
    var msg: String? = null
    var version: String? = null

    val isSuccess: Boolean
        get() = Utils.isNotEmpty(this) && status == 0
}
