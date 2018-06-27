package kz.vozon.vozon.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import kz.vozon.vozon.R
import kz.vozon.vozon.models.User
import kz.vozon.vozon.ui.auth.SendSmsActivity
import kz.vozon.vozon.utility.Api
import kz.vozon.vozon.utility.Shared
import kz.vozon.vozon.utility.norm
import kz.vozon.vozon.utility.run2


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        Shared.init(PreferenceManager.getDefaultSharedPreferences(applicationContext))

        if (Shared.currentUser.token == null) {
            next()
            return
        }

        val test = when (Shared.currentUser.type) {
            kz.vozon.vozon.models.User.CLIENT -> Api.clientService.test()
            kz.vozon.vozon.models.User.COURIER -> Api.courierService.test()
            else -> {
                Shared.currentUser = kz.vozon.vozon.models.User()
                next()
                return
            }
        }

        test.run2(this,
                { body ->
                    body.token = Shared.currentUser.token
                    Shared.currentUser = body
                    next()
                },
                { code, _ ->
                    if (code == 99 || code == 404) {
                        Shared.currentUser = kz.vozon.vozon.models.User()
                        next()
                    }
                })
    }

    fun next() {
        val i = Intent(this, kz.vozon.vozon.ui.auth.SendSmsActivity::class.java)
        val action = intent.getStringExtra(Shared.action)
        norm(action)
        i.putExtra(Shared.action, action)
        startActivityForResult(i, 45)
        this.overridePendingTransition(0, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        finish()
    }
}