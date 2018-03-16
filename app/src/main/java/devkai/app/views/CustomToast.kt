package devkai.app.views

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import devkai.app.R

class CustomToast
/**
 * Construct an empty Toast object.  You must call [.setView] before you
 * can call [.show].
 *
 * @param context The context to use.  Usually your [android.app.Application]
 * or [android.app.Activity] object.
 */
(context: Context) : Toast(context) {
    companion object {

        @JvmOverloads
        fun makeText(context: Context?, info: String, duration: Int, gravity: Int = Gravity.CENTER): Toast? {
            if (context == null)
                return null

            val toast = CustomToast(context)

            val inflater = LayoutInflater.from(context)
            val toastLayout = inflater.inflate(R.layout.custom_toast, null) as ViewGroup
            val contentTxt = toastLayout.getChildAt(0) as TextView
            contentTxt.text = info
            contentTxt.gravity = gravity

            toast.view = toastLayout
            toast.duration = duration
            toast.setGravity(gravity, 0, 0)
            return toast
        }

        fun makeText(context: Context?, resId: Int, duration: Int): Toast? {
            return if (context == null) null else makeText(context, resId, duration, Gravity.CENTER)

        }

        fun makeText(context: Context?, resId: Int, duration: Int, gravity: Int): Toast? {
            return if (context == null) null else makeText(context, context.resources.getString(resId), duration, gravity)

        }
    }

}
