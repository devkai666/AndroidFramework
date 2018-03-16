package devkai.app.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.google.inject.Key
import devkai.app.R
import roboguice.RoboGuice
import roboguice.activity.event.*
import roboguice.event.EventManager
import roboguice.util.RoboContext
import java.util.*


open class BaseActivity : AppCompatActivity(), RoboContext {

    protected lateinit var eventManager: EventManager
    protected var scopedObjects = HashMap<Key<*>, Any>()
    lateinit var actionBarLayout: View
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        context = this
        val injector = RoboGuice.getInjector(this)
        eventManager = injector.getInstance(EventManager::class.java)
        injector.injectMembersWithoutViews(this)
        super.onCreate(savedInstanceState)
        eventManager.fire(OnCreateEvent(savedInstanceState))
        initActionBar()
        //禁用横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    protected fun initActionBar() {
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.hideOffset = 0
        supportActionBar!!.setCustomView(R.layout.layout_actionbar)

        actionBarLayout = supportActionBar!!.customView
        actionBarLayout.findViewById<View>(R.id.go_back_layout).setOnClickListener { onBackPressed() }
        (actionBarLayout.findViewById<View>(R.id.action_bar_title) as TextView).text = title
    }

    fun setActionBarTitle(title: CharSequence) {
        (actionBarLayout.findViewById<View>(R.id.action_bar_title) as TextView).text = title
    }

    override fun onRestart() {
        super.onRestart()
        eventManager.fire(OnRestartEvent())
    }

    override fun onStart() {
        super.onStart()
        eventManager.fire(OnStartEvent())
    }

    override fun onResume() {
        super.onResume()
        eventManager.fire(OnResumeEvent())
    }

    override fun onPause() {
        super.onPause()
        eventManager.fire(OnPauseEvent())
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        eventManager.fire(OnNewIntentEvent())
        setIntent(intent)
    }

    override fun onStop() {
        try {
            eventManager.fire(OnStopEvent())
        } finally {
            super.onStop()
        }
    }

    override fun onDestroy() {
        try {
            eventManager.fire(OnDestroyEvent())
        } finally {
            try {
                RoboGuice.destroyInjector(this)
            } finally {
                super.onDestroy()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val currentConfig = resources.configuration
        super.onConfigurationChanged(newConfig)
        eventManager.fire(OnConfigurationChangedEvent(currentConfig, newConfig))
    }

    override fun onSupportContentChanged() {
        super.onSupportContentChanged()
        try {
            RoboGuice.getInjector(this).injectViewMembers(this)
            eventManager.fire(OnContentChangedEvent())
        } catch (e: Exception) {
        }

    }

    override fun getScopedObjectMap(): Map<Key<*>, Any> {
        return scopedObjects
    }

    protected fun onFragmentShow(fragment: Fragment) {
        if (fragment is ITitle) {
            val title = fragment as ITitle
            setActionBarTitle(title.title)
        }
    }
}


