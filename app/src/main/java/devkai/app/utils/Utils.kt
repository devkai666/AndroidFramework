package devkai.app.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import devkai.app.views.CustomToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    /**
     * 单位转换
     * dp转换为px
     */
    fun dp2px(context: Context, dp: Int): Int {
        val metrics = context.resources.displayMetrics
        return (dp * metrics.density).toInt()
    }

    /**
     * 获取屏幕dpi
     */
    fun getDpi(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.densityDpi
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    /**
     * 获取屏幕高度
     */
    fun getWindowHeight(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.heightPixels
    }

    /**
     * 获取屏幕宽度
     */
    fun getWindowWidth(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.widthPixels
    }

    /**
     * 获取控件宽
     */
    fun getWidth(view: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return view.measuredWidth
    }

    /**
     * 获取时间格式字符串
     *
     * @param timeStamp
     * @param pattern
     * @return
     */
    fun getDateFormat(timeStamp: Long, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern)
        val date = Date(timeStamp * 1000)
        return dateFormat.format(date)
    }

    /**
     * 获取时间戳
     *
     * @param dateStr
     * @param pattern yyyy MM dd HH mm ss
     * @return
     */
    @Throws(ParseException::class)
    fun getTimeStamp(dateStr: String, pattern: String): Long {
        val dateFormat = SimpleDateFormat(pattern)
        val date = dateFormat.parse(dateStr)
        return date.time
    }

    /**
     * 显示错误信息
     */
    fun showError(context: Context, errRes: String) {
        CustomToast.makeText(context, errRes, Toast.LENGTH_SHORT)?.show()
    }

    /**
     * String转换为List
     *
     * @param pattern 分割样式
     */
    fun stringToList(str: String, pattern: String): List<String> {
        val stringList = str.split(pattern.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().toList()
        return stringList
    }

    /**
     * 连接字符串
     *
     * @param iterator
     * @param separator 分割样式
     */
    fun join(iterator: Iterator<*>?, separator: String?): String? {
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null
        }
        if (!iterator.hasNext()) {
            return ""
        }
        val first = iterator.next()
        if (!iterator.hasNext()) {
            return first!!.toString()
        }

        // two or more elements
        val buf = StringBuffer(256) // Java default is 16, probably too small
        if (first != null) {
            buf.append(first)
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator)
            }
            val str = iterator.next()
            if (str != null) {
                buf.append(str)
            }
        }

        return buf.toString()
    }

    /**
     * 判断对象是否为空
     */
    fun isNotEmpty(`object`: Any?): Boolean {
        val isNotEmpty: Boolean = when (`object`) {
            is List<*> -> !`object`.isEmpty()
            is String -> !`object`.isEmpty()
            else -> `object` != null
        }
        return isNotEmpty
    }

    /**
     * 获取Header Accept
     *
     * @param version 版本
     * @return
     */
    fun getAcceptHeader(version: String): String {
        return "application/devkai-json-version:" + version
    }
}
