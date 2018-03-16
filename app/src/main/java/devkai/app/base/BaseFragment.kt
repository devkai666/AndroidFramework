package devkai.app.base

import android.content.res.Resources
import android.view.View

import devkai.app.utils.Utils
import roboguice.fragment.RoboFragment

class BaseFragment : RoboFragment(), ITitle {

    val actionBarLayout: View?
        get() {
            val activity = activity as BaseActivity
            var view: View? = null
            if (Utils.isNotEmpty(activity)) {
                view = activity.actionBarLayout
            }
            return view
        }

    val res: Resources?
        get() {
            var resources: Resources? = null
            if (isAdded) {
                resources = getResources()
            }
            return resources
        }

    override fun getTitle(): CharSequence {
        return activity.title
    }

    fun getStr(resId: Int): String {
        var str = ""
        if (isAdded) {
            str = getString(resId)
        }
        return str
    }

    fun getStr(resId: Int, vararg formatArgs: Any): String {
        var str = ""
        if (isAdded) {
            str = getString(resId, *formatArgs)
        }
        return str
    }
}
