package kz.vozon.vozon.ui.info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_terms.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.addBackPress

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        addBackPress()

        v_web.loadUrl("http://188.166.50.157:8000/info/terms")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
