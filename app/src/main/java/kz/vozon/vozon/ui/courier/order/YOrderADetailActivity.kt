package kz.vozon.vozon.ui.courier.order

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_y_order_a_detail.*
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.client.route.XRouteDetailActivity.Companion.REQUEST_PHONE_CALL
import kz.vozon.vozon.utility.*


class YOrderADetailActivity : AppCompatActivity() {

    private var order: kz.vozon.vozon.models.Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_y_order_a_detail)
        addBackPress()

        order = intent.get(kz.vozon.vozon.models.Order::class.java)

        supportActionBar!!.title = order!!.title

        val list = listOf(order!!.image1,order!!.image2).filter { it != null }
        if(list.isNotEmpty()) v_images.adapter = kz.vozon.vozon.ui.ImagePagerAdapter(this, list)
        else v_images.visibility = View.GONE
        

        v_avatar.src(order?.owner?.avatar,R.drawable.user_placeholder)
        v_name.text = order?.owner?.name
        v_date.text = order!!.shippingDate!!.dateFormat(order!!.shippingTime!!)

        v_start_point.text = order!!.startPoint!!.getShortName(order!!.startDetail!!)
        v_end_point.text = order!!.endPoint!!.getShortName(order!!.endDetail!!)

        v_volume.text = getString(R.string.meter3,order!!.width!! * order!!.height!!*order!!.length!!)
        v_mass.text = if(order!!.mass!!>1){
            getString(R.string.kg,order!!.mass!!)
        }else{
            getString(R.string.g,order!!.mass!!*1000)
        }
        v_price.text = getString(R.string._s_,order!!.price.toString(),order!!.currency)

        v_position.text = order!!.startPoint!! - order!!.endPoint!!
        v_category.text = order!!.typeName
        v_payment_type.text = order!!.paymentTypeName

        v_comment.text = order!!.comment

        val offer = order!!.offer!!
        v_image.src(offer.transport?.type?.icon,R.drawable.tmp_truck)
        v_transport.text = offer.transport?.fullName
        v_t_type.text = getString(R.string._o_,offer.transport?.type?.name,offer.transport?.loadTypeName)
        v_price2.text = getString(R.string._s_, offer.price.toString(),offer.currency)
        v_payment_type2.text = offer.paymentTypeName
        v_other_service.text = offer.otherServiceName
        v_have_loaders.text = yesOrNo(offer.haveLoaders)
        v_comment2.text = offer.comment

        v_name2.text = order?.acceptPerson

        v_call.setOnClickListener{
            offer.transport!!.owner!!.callIntent()
            if(askPermission(Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL))
                startActivity(order?.callIntent())
        }


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
            kz.vozon.vozon.ui.client.route.XRouteDetailActivity.REQUEST_PHONE_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(order!!.offer!!.transport!!.owner!!.callIntent())
                }
            }
        }
    }
}
