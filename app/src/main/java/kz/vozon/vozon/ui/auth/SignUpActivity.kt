package kz.vozon.vozon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import kz.vozon.vozon.R

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        v_client.setOnClickListener{
            val i = Intent(this, kz.vozon.vozon.ui.auth.XSignUpActivity::class.java)
            startActivity(i)
        }

        v_courier.setOnClickListener{
            val i = Intent(this, kz.vozon.vozon.ui.auth.YSignUpActivity::class.java)
            startActivity(i)
        }
    }
}
