package kz.vozon.vozon.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_client_signup.*
import kotlinx.android.synthetic.main.include_sign_up_main.*
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.info.LocationActivity
import kz.vozon.vozon.utility.*
import java.io.File


class XSignUpActivity : AppCompatActivity() {

    companion object {
        const val CITY_REQUEST_CODE = 86
        const val FINISH_REQUEST_CODE = 100
        const val IMAGE_REQUEST_CODE = 196
    }

    private val user = kz.vozon.vozon.models.User()
    private var image: File? = null

    var phoneOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup)
        addBackPress()

        user.type = kz.vozon.vozon.models.User.CLIENT

        v_city.setOnClickListener {
            val i = Intent(this@XSignUpActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            startActivityForResult(i, CITY_REQUEST_CODE)
        }
        v_upload_image.setOnClickListener{
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE_REQUEST_CODE)
        }


        var countries: List<kz.vozon.vozon.models.Country>
        Api.infoService.countryPhone().run3(this) {
            countries = it
            after(countries)
        }
    }


    private fun after(countries: List<kz.vozon.vozon.models.Country>){
        var country: kz.vozon.vozon.models.Country
        val adapter = kz.vozon.vozon.ui.CodeAdapter(this, countries)
        var listener: MaskedTextChangedListener? = null

        v_spinner.adapter = adapter
        v_spinner.setSelection(0)
        v_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                v_phone_number.text.clear()
                country = countries[position]
                v_phone_number.removeTextChangedListener(listener)
                listener = MaskedTextChangedListener(
                        country.phoneMask!!,
                        v_phone_number,
                        object : MaskedTextChangedListener.ValueListener {
                            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                                user.phone =  country.phoneCode + extractedValue
                                phoneOk = maskFilled
                            }
                        }
                )
                v_phone_number.addTextChangedListener(listener)
                v_phone_number.onFocusChangeListener = listener
            }

        }
        v_spinner.onItemSelectedListener
                .onItemSelected(null,null,v_spinner.selectedItemPosition,0)
    }

    private fun done(){

        try{
            checkNotNull(image){getString(R.string.enter_photo)}
            if(phoneOk.not()) error(getString(R.string.enter_phone))
            user.name = v_name.get(getString(R.string.enter_name))
            checkNotNull(user.city){getString(R.string.enter_city)}
            user.about = v_about.get()


            val phone = user.phone!!.toMultiPart()
            val name = user.name!!.toMultiPart()
            val city = user.city!!.id.toString().toMultiPart()
            val about = user.about.toMultiPart()
            val type = user.type!!.toMultiPart()
            val image = image!!.toMultiPartImage("avatar")

            Api.authService.register(phone,name,city,about,type,image).run2(this,{
                val i = Intent(this, kz.vozon.vozon.ui.auth.LoginActivity::class.java)
                i.put(user.phone!!)
                startActivityForResult(i,FINISH_REQUEST_CODE)
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
