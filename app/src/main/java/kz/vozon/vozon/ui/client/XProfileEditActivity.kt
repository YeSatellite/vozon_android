package kz.vozon.vozon.ui.client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.theartofdev.edmodo.cropper.CropImage
import kz.vozon.vozon.R
import kz.vozon.vozon.models.Location
import kz.vozon.vozon.ui.info.LocationActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.activity_client_signup.*
import kotlinx.android.synthetic.main.include_sign_up_main.*
import java.io.File


class XProfileEditActivity : AppCompatActivity() {

    companion object {
        const val CITY_REQUEST_CODE = 86
        const val FINISH_REQUEST_CODE = 100
        const val IMAGE_REQUEST_CODE = 196
    }

    private val user = Shared.currentUser.clone()
    private var image: File? = null

    var phoneOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup)
        addBackPress()

        v_city.setOnClickListener {
            val i = Intent(this@XProfileEditActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            startActivityForResult(i, CITY_REQUEST_CODE)
        }
        v_upload_image.setOnClickListener{
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE_REQUEST_CODE)
        }

        v_phone.visibility = View.GONE


        v_image.src(user.avatar,R.drawable.user_placeholder)
        v_name.content = user.name
        v_city.content = user.city?.getShortName()
        v_about.content = user.about


    }

    private fun done(){

        try{
            user.name = v_name.get("name is empty")
            user.about = v_about.get()
            checkNotNull(user.city){"city is empty"}

            val name = user.name!!.toMultiPart()
            val city = user.city!!.id.toString().toMultiPart()
            val about = user.about.toMultiPart()
            val image = image.toMultiPartImage("avatar")

            Api.clientService.profileUpdate(name,city,about,image).run2(this,{
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CITY_REQUEST_CODE -> {
                    val location = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    user.city = location
                    v_city.content = location.getShortName()
                }
                IMAGE_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    v_image.setImageURI(imageUri)
                    image = compressImage(imageUri)
                }
            }
        }

        if(requestCode == FINISH_REQUEST_CODE){
            finish()
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
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
