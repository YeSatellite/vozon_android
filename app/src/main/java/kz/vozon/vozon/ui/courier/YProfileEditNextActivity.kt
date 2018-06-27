package kz.vozon.vozon.ui.courier

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kz.vozon.vozon.R
import kz.vozon.vozon.models.User
import kz.vozon.vozon.ui.BackPressCompatActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.activity_client_signup_next.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


class YProfileEditNextActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    private var user: kz.vozon.vozon.models.User? = null
    private var image: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup_next)

        user = intent.get(kz.vozon.vozon.models.User::class.java)
        try{
            image = intent.get(File::class.java)
        }catch (ex: kotlin.TypeCastException){

        }


        v_experience.content = user!!.experience.toString()

    }

    private fun done(){
        try{
            user!!.courier_type = when(v_courier_type.checkedRadioButtonId) {
                R.id.v_natural_person -> 1
                R.id.v_juridical_person -> 2
                else -> error(getString(R.string.something_went_wrong))
            }
            user!!.experience = v_experience.get(getString(R.string.enter_experience)).toLong()

            val name =  user!!.name!!.toMultiPart()
            val city =  user!!.city!!.id.toString().toMultiPart()
            val about = user!!.about.toMultiPart()
            val image = image?.toMultiPartImage("avatar")
            val courierType =  user!!.courier_type!!.toString().toMultiPart()
            val experience =  user!!.experience!!.toString().toMultiPart()
            Api.courierService.profileUpdate(name,city,about,image,courierType,experience)
                    .run2(this,{
                        it.token = Shared.currentUser.token
                        Shared.currentUser = it
                        setResult(Activity.RESULT_OK)
                        finish()
            },{ _, error ->
                snack(error)
            })


        }catch (ex: IllegalStateException){
            snack(ex.message ?: getString(R.string.something_went_wrong))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                done()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
