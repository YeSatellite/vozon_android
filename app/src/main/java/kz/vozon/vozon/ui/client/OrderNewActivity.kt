package kz.vozon.vozon.ui.client

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.theartofdev.edmodo.cropper.CropImage
import kz.vozon.vozon.R
import kz.vozon.vozon.models.*
import kz.vozon.vozon.ui.BackPressCompatActivity
import kz.vozon.vozon.ui.CodeAdapter
import kz.vozon.vozon.ui.info.InfoTmpActivity
import kz.vozon.vozon.ui.info.LocationActivity
import kz.vozon.vozon.utility.*
import kz.vozon.vozon.utility.Shared.currencies
import kotlinx.android.synthetic.main.activity_order_new.*
import java.io.File


class OrderNewActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {
    companion object {
        const val START_POINT_REQUEST_CODE = 54
        const val END_POINT_REQUEST_CODE = 57
        const val PAYMENT_TYPE_REQUEST_CODE = 86
    }


    private val order = kz.vozon.vozon.models.Order()
    private var image1: File? = null
    private var image2: File? = null
    private var imageCur = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_new)
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE,750)

        order.type = intent.get(kz.vozon.vozon.models.TType::class.java).id

        v_image1!!.setOnClickListener {
            imageCur = 1
            CropImage.startPickImageActivity(this)
        }
        v_image2!!.setOnClickListener {
            imageCur = 2
            CropImage.startPickImageActivity(this)
        }

        v_start_point.setOnClickListener {
            val i = Intent(this@OrderNewActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            i.putExtra(kz.vozon.vozon.ui.info.LocationActivity.EXTRA, kz.vozon.vozon.ui.info.LocationActivity.EXTRA)
            startActivityForResult(i,START_POINT_REQUEST_CODE)
        }
        v_end_point.setOnClickListener {
            val i = Intent(this@OrderNewActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            i.putExtra(kz.vozon.vozon.ui.info.LocationActivity.EXTRA, kz.vozon.vozon.ui.info.LocationActivity.EXTRA)
            startActivityForResult(i,END_POINT_REQUEST_CODE)
        }
        v_shipping_date.setOnClickListener(setDateListener(this))
        v_shipping_time.setOnClickListener(setTimeListener(this))
        v_transport.setOnClickListener {
            val i = Intent(this@OrderNewActivity, kz.vozon.vozon.ui.info.InfoTmpActivity::class.java)
            Shared.call = Api.infoService.paymentType()
            i.putExtra(Shared.title,getString(R.string.payment_type))
            startActivityForResult(i,PAYMENT_TYPE_REQUEST_CODE)
        }

        val currencies = currencies()

        val adapter = kz.vozon.vozon.ui.CodeAdapter(this, currencies)

        v_spinner.adapter = adapter
        v_spinner.setSelection(0)
        v_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                order.currency = currencies[position].phoneCode
            }

        }
        v_spinner.onItemSelectedListener
                .onItemSelected(null,null,v_spinner.selectedItemPosition,0)
    }

    private fun create(){
        try{
            order.title = v_title.get(getString(R.string.enter_title))
            order.comment = v_comment.get(getString(R.string.enter_cargo))
            order.length = v_length.get(getString(R.string.enter_length)).toFloat()
            order.width = v_width.get(getString(R.string.enter_width)).toFloat()
            order.height = v_height.get(getString(R.string.enter_height)).toFloat()
            order.mass = v_mass.get(getString(R.string.enter_mass)).toFloat()
            check(order.startPoint != null){getString(R.string.enter_start_point)}
            check(order.endPoint != null){getString(R.string.enter_end_point)}
            order.shippingDate = v_shipping_date.get(getString(R.string.enter_date))
            order.shippingTime = v_shipping_time.get(getString(R.string.enter_time))
            order.acceptPerson = v_accept_person.get(getString(R.string.enter_accept_person))
            order.acceptPersonContact = v_accept_person_contact.get(getString(R.string.enter_accept_person_contact))
            order.price = v_price.get(getString(R.string.enter_price)).toFloat()


            Api.clientService.orderAdd(order).run2(this,{ body ->
                updateImage(body.id!!)
            },{ _, error ->
                snack(error)
            })

        }catch (ex: IllegalStateException){
            snack(ex.message ?: getString(R.string.something_went_wrong))
        }
    }

    private fun updateImage(id: Long) {
        val image1 = image1.toMultiPartImage("image1")
        val image2 = image2.toMultiPartImage("image2")

        if(image1!=null || image2!=null)
            Api.clientService.orderUpdate(id,image1,image2).run2(this,{
                setResult(Activity.RESULT_OK)
                finish()
            },{ _, error ->
                snack(error)
            })
        else{
            setResult(Activity.RESULT_OK)
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
                create()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }























    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    when(imageCur){
                        1 -> {
                            image1 = compressImage(imageUri)
                            v_image1.setImageURI(imageUri)
                        }
                        2 -> {
                            image2 = compressImage(imageUri)
                            v_image2.setImageURI(imageUri)
                        }
                    }

                }
                START_POINT_REQUEST_CODE -> {
                    order.startPoint = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    order.startDetail = data.getStringExtra(kz.vozon.vozon.ui.info.LocationActivity.EXTRA)
                    v_start_point.content = order.startPoint!!.getShortXName(order.startDetail!!)
                }
                END_POINT_REQUEST_CODE -> {
                    order.endPoint = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    order.endDetail = data.getStringExtra(kz.vozon.vozon.ui.info.LocationActivity.EXTRA)
                    v_end_point.content = order.endPoint!!.getShortXName(order.endDetail!!)
                }
                PAYMENT_TYPE_REQUEST_CODE -> {
                    val paymentType =  data!!.get(kz.vozon.vozon.models.InfoTmp::class.java)
                    order.paymentType = paymentType.id
                    v_transport.setText(paymentType.name, TextView.BufferType.EDITABLE)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            750 -> if ((grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED))
                finish()
            else ->{}

        }
    }
}









