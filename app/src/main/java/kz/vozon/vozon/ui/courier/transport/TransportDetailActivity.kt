package kz.vozon.vozon.ui.courier.transport

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_transport_detail.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.*


class TransportDetailActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {
    var transport: kz.vozon.vozon.models.Transport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_detail)


        transport = intent.get(kz.vozon.vozon.models.Transport::class.java)

        supportActionBar!!.title = transport!!.number
        v_images.adapter = kz.vozon.vozon.ui.ImagePagerAdapter(
                this,
                listOf(transport!!.image1, transport!!.image2))

        v_length.text = getString(R.string.meter,transport!!.length)
        v_width.text = getString(R.string.meter,transport!!.width)
        v_height.text = getString(R.string.meter,transport!!.height)
        v_number.text = transport!!.number
        v_mark.text = transport!!.markName
        v_model.text = transport!!.modelName
        v_t_type.text = transport!!.type?.name
        v_load_type.text = transport!!.loadTypeName
        v_comment.text = transport!!.comment

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(intent.getBooleanExtra("have",true))
            menuInflater.inflate(R.menu.menu_remove, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove -> {
                ask {
                    Api.courierService.transportDelete(transport!!.id!!).run2(this, {
                    }, { code, error ->
                        if (code == 0) {
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            snack(error)
                        }
                    })
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
