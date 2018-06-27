package kz.vozon.vozon.ui.courier

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kz.vozon.vozon.R
import kz.vozon.vozon.models.User
import kz.vozon.vozon.ui.BackPressCompatActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.activity_setting.*

class YSettingActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        v_logout.setOnClickListener{
            ask {
                Shared.currentUser = kz.vozon.vozon.models.User()
                setResult(Activity.RESULT_OK)
                this.finish()
            }
        }

        v_edit.setOnClickListener{
            startActivity(Intent(this, kz.vozon.vozon.ui.courier.YProfileEditActivity::class.java))
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
}
