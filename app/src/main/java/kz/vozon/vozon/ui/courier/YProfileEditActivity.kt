package kz.vozon.vozon.ui.courier

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_client_signup.*
import kotlinx.android.synthetic.main.include_sign_up_main.*
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.info.LocationActivity
import kz.vozon.vozon.utility.*
import java.io.File


class YProfileEditActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    companion object {
        const val CITY_REQUEST_CODE = 86
        const val IMAGE_REQUEST_CODE = 196
        const val NEXT_REQUEST_CODE = 197
    }

    private val user = Shared.currentUser.clone()
    private var image: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup)

        v_city.setOnClickListener {
            val i = Intent(this@YProfileEditActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
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

    private fun next(){
        try{
            user.name = v_name.get(getString(R.string.enter_name))
            user.about = v_about.get()

            val i = Intent(this, kz.vozon.vozon.ui.courier.YProfileEditNextActivity::class.java)
            i.put(user)
            if(image != null) i.put(image!!)
            startActivityForResult(i,NEXT_REQUEST_CODE)


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
                NEXT_REQUEST_CODE -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_next, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next -> {
                next()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
