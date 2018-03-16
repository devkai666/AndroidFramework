package devkai.app.main

import android.os.Bundle
import android.view.View
import devkai.app.R
import devkai.app.base.BaseActivity
import kotlinx.android.synthetic.main.layout_actionbar.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        go_back_layout?.visibility = View.GONE
    }
}
