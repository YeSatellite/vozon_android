package kz.vozon.vozon.ui.client.active

import android.os.Bundle
import android.support.v7.app.AlertDialog
import kz.vozon.vozon.R
import kz.vozon.vozon.models.Order
import kz.vozon.vozon.ui.BackPressCompatActivity
import kz.vozon.vozon.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.activity_order_a_detail.*
import android.view.View
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.item_rating.view.*


class XOrderADetailActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    private var order: kz.vozon.vozon.models.Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_a_detail)

        order = intent.get(kz.vozon.vozon.models.Order::class.java)

        supportActionBar!!.title = order!!.title

        val list = listOf(order!!.image1,order!!.image2).filter { it != null }
        if(list.isNotEmpty()) v_images.adapter = kz.vozon.vozon.ui.ImagePagerAdapter(this, list)
        else v_images.visibility = View.GONE

        v_date.text = getString(R.string._o_,
                order!!.shippingDate!!.dateFormat(),
                order!!.shippingTime!!.substring(0..4))
        v_start_point.text = order!!.startPoint!!.getShortName(order!!.startDetail!!)
        v_end_point.text = order!!.endPoint!!.getShortName(order!!.endDetail!!)

        v_volume.text = getString(R.string.meter3,order!!.width!! * order!!.height!!*order!!.length!!)
        v_mass.text = if(order!!.mass!!>1){
            getString(R.string.kg,order!!.mass!!)
        }else{
            getString(R.string.g,order!!.mass!!*1000)
        }

        v_position.text = order!!.startPoint!! - order!!.endPoint!!
        v_category.text = order!!.typeName
        v_payment_type.text = order!!.paymentTypeName

        v_comment.text = order!!.comment

        val offer = order!!.offer!!
        v_image.src(offer.transport?.type?.icon,R.drawable.tmp_truck)
        v_transport.text = offer.transport?.fullName
        v_t_type.text = offer.transport?.type?.name
        v_price2.text = getString(R.string._s_, offer.price.toString(),offer.currency)
        v_payment_type2.text = offer.paymentTypeName
        v_other_service.text = offer.otherServiceName
        v_have_loaders.text = yesOrNo(offer.haveLoaders)
        v_comment2.text = offer.comment

        v_button.text = getString(R.string.delivered)
        v_button.setOnClickListener{
            val v = View.inflate(this,R.layout.item_rating,null)
            val popDialog = AlertDialog.Builder(this)
            popDialog.setTitle("Оцените работу курьера")
            popDialog.setView(v)
            popDialog.setPositiveButton(android.R.string.ok) { dialog, _ ->
                Api.clientService.orderDone(order!!.id!!,v.v_rating.progress)
                        .run3(this){
                            setResult(RESULT_OK)
                            finish()
                        }
                dialog.dismiss()
            }.setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.cancel() })

            popDialog.create()
            popDialog.show()


        }

    }
}
