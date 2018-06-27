package kz.vozon.vozon.ui.courier.transport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.theartofdev.edmodo.cropper.CropImage
import kz.vozon.vozon.R
import kz.vozon.vozon.models.InfoTmp
import kz.vozon.vozon.models.Transport
import kz.vozon.vozon.ui.BackPressCompatActivity
import kz.vozon.vozon.ui.info.InfoTmpActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.activity_transport_new.*
import java.io.File

class TransportNewActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    companion object {
        const val MARK_REQUEST_CODE = 57
        const val MODEL_REQUEST_CODE = 84
        const val TYPE_TYPE_REQUEST_CODE = 89
        const val LOAD_TYPE_REQUEST_CODE = 96
        const val IMAGE1_REQUEST_CODE = 12
        const val IMAGE2_REQUEST_CODE = 13
    }

    private val transport = kz.vozon.vozon.models.Transport()
    private val images = Array<File?>(2){null}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_new)

        v_mark.setOnClickListener {
            val i = Intent(this@TransportNewActivity, kz.vozon.vozon.ui.info.InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tMark()
            i.putExtra("title",getString(R.string.mark))
            startActivityForResult(i, MARK_REQUEST_CODE)
        }
        v_model.setOnClickListener {
            val i = Intent(this@TransportNewActivity, kz.vozon.vozon.ui.info.InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tModel(transport.mark ?: run {
                snack("Choose mark")
                return@setOnClickListener
            })
            i.putExtra("title",getString(R.string.model))
            startActivityForResult(i, MODEL_REQUEST_CODE)
        }

        v_t_type.setOnClickListener {
            val i = Intent(this@TransportNewActivity, kz.vozon.vozon.ui.info.InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tTypeTmp()
            i.putExtra("title",getString(R.string.t_type))
            startActivityForResult(i, TYPE_TYPE_REQUEST_CODE)
        }

        v_load_type.setOnClickListener {
            val i = Intent(this@TransportNewActivity, kz.vozon.vozon.ui.info.InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tLoadType()
            i.putExtra("title",getString(R.string.load_type))
            startActivityForResult(i, LOAD_TYPE_REQUEST_CODE)
        }

        v_image1.setOnClickListener{
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE1_REQUEST_CODE)
        }
        v_image2.setOnClickListener {
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE2_REQUEST_CODE)
        }
    }


    private fun create(){
        try{
            checkNotNull(images[0]){getString(R.string.enter_photo)}
            checkNotNull(images[1]){getString(R.string.enter_photo)}
            transport.number = v_number.get("Введите номер авто")
            checkNotNull(transport.model){"Выберите марку"}
            checkNotNull(transport.typeId){getString(R.string.enter_t_type)}
            transport.length = v_length.get(getString(R.string.enter_length)).toFloat()
            transport.width = v_width.get(getString(R.string.enter_width)).toFloat()
            transport.height = v_height.get(getString(R.string.enter_height)).toFloat()
            checkNotNull(transport.typeId){getString(R.string.enter_t_type)}
            checkNotNull(transport.loadType){"Выберите вид погрузки"}
            transport.comment = v_comment.get()
            Api.courierService.transportsAdd(transport).run3(this){ body ->
                updateImage(body.id!!)
            }

        }catch (ex: IllegalStateException){
            snack(ex.message ?: getString(R.string.something_went_wrong))
        }
    }

    private fun updateImage(id: Long) {
        val image1 = images[0]!!.toMultiPartImage("image1")
        val image2 = images[1]!!.toMultiPartImage("image2")

        Api.courierService.transportsUpdate(id,image1,image2).run2(this,{
            setResult(Activity.RESULT_OK, Intent())
            finish()
        },{ _, error ->
            snack(error)
        })
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                MARK_REQUEST_CODE -> {
                    val mark =  data!!.get(kz.vozon.vozon.models.InfoTmp::class.java)
                    transport.mark = mark.id
                    v_mark.content = mark.name ?: ""
                }
                MODEL_REQUEST_CODE -> {
                    val model =  data!!.get(kz.vozon.vozon.models.InfoTmp::class.java)
                    transport.model = model.id
                    v_model.content = model.name ?: ""
                }
                TYPE_TYPE_REQUEST_CODE -> {
                    val type =  data!!.get(kz.vozon.vozon.models.InfoTmp::class.java)
                    transport.typeId = type.id
                    v_t_type.content = type.name ?: ""
                }
                LOAD_TYPE_REQUEST_CODE -> {
                    val loadType =  data!!.get(kz.vozon.vozon.models.InfoTmp::class.java)
                    transport.loadType = loadType.id
                    v_load_type.content = loadType.name ?: ""
                }
                IMAGE1_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    v_image1.setImageURI(imageUri)
                    images[0] = compressImage(imageUri)
                }
                IMAGE2_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    v_image2.setImageURI(imageUri)
                    images[1] = compressImage(imageUri)
                }
            }
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

}











