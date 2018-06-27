package kz.vozon.vozon.ui.client.posted

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_order_posted_detail.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.*


class XOrderPDetailActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {
    var order: kz.vozon.vozon.models.Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_posted_detail)


        val order = intent.get(kz.vozon.vozon.models.Order::class.java)
        this.order = order

        supportActionBar!!.title = order.title

        val list = listOf(order.image1, order.image2).filter { it != null }
        if(list.isNotEmpty()) v_images.adapter = kz.vozon.vozon.ui.ImagePagerAdapter(this, list)
        else v_images.visibility = View.GONE

        v_date.text = order.shippingDate!!.dateFormat()
        v_start_point.text = order.startPoint!!.getShortName(order.startDetail!!)
        v_end_point.text = order.endPoint!!.getShortName(order.endDetail!!)

        v_volume.text = getString(R.string.meter3,order.width!! * order.height!!*order.length!!)
        v_mass.text = if(order.mass!!>1){
            getString(R.string.kg, order.mass!!)
        }else{
            getString(R.string.g, order.mass!!*1000)
        }

        v_position.text = order.startPoint!! - order.endPoint!!
        norm(order.type.toString())
        norm(order.typeName)
        v_t_type.text = order.typeName
        v_transport.text = order.paymentTypeName

        v_comment.text = order.comment

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_remove, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove -> {
                ask {
                    Api.clientService.orderDelete(order!!.id!!).run2(this, {
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
