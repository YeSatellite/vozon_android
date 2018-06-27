package kz.vozon.vozon.ui.client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.*

class XSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        addBackPress()

        v_logout.setOnClickListener{
            ask {
                Shared.currentUser = kz.vozon.vozon.models.User()
                setResult(Activity.RESULT_OK)
                this.finish()
            }
        }

        v_edit.setOnClickListener{
            startActivity(Intent(this, kz.vozon.vozon.ui.client.XProfileEditActivity::class.java))
        }


        v_remove_user.setOnClickListener {
            ask {
                Api.authService.remove_user().run3(this){
                    alert("Аккаунт успешно удалено"){
                        Shared.currentUser = kz.vozon.vozon.models.User()
                        setResult(Activity.RESULT_OK)
                        this.finish()
                    }
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
