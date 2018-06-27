package kz.vozon.vozon.ui.client.route

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kz.vozon.vozon.R
import kz.vozon.vozon.models.Route
import kz.vozon.vozon.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.activity_x_route_detail.*
import android.content.pm.PackageManager
import kz.vozon.vozon.utility.*


class XRouteDetailActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_PHONE_CALL = 24
    }

    private var route: kz.vozon.vozon.models.Route? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x_route_detail)
        addBackPress()

        route = intent.get(kz.vozon.vozon.models.Route::class.java)

        supportActionBar!!.title = route!!.transport!!.modelName

        v_images.adapter = kz.vozon.vozon.ui.ImagePagerAdapter(
                this,
                listOf(route?.transport?.image1, route?.transport?.image2))

        v_avatar.src(route?.owner?.avatar,R.drawable.user_placeholder)
        v_name.text = route?.owner?.name
        v_rationg.text = if(route?.owner?.rating.isNullOrEmpty())
            route?.owner?.courierTypeName
        else
            getString(R.string._o_,route?.owner?.courierTypeName,route?.owner?.rating)
        v_call.setOnClickListener{
            if(askPermission(Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL))
                startActivity(route!!.owner!!.callIntent())
        }

        v_date.text = route?.shippingDate?.dateFormat(route?.shippingTime!!)
        v_start_point.text = route?.startPoint!!.getShortName()
        v_end_point.textIf = route!!.endPoint?.getShortName()

        v_number.text = route?.transport?.number
        v_mark.text = route?.transport?.markName
        v_model.text = route?.transport?.modelName
        v_t_type.text = route?.transport?.type?.name
        v_length.text = getString(R.string.meter,route?.transport?.length)
        v_width.text = getString(R.string.meter,route?.transport?.width)
        v_height.text = getString(R.string.meter,route?.transport?.height)
        v_load_type.text = route?.transport?.loadTypeName

        v_comment.text = route!!.comment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PHONE_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(route!!.owner!!.callIntent())
                }
            }
        }
    }
}
