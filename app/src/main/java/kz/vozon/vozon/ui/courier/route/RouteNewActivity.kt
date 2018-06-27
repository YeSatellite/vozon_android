package kz.vozon.vozon.ui.courier.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_route_new.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.*

class RouteNewActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    companion object {
        const val START_POINT_REQUEST_CODE = 54
        const val END_POINT_REQUEST_CODE = 57
        const val TRANSPORT_REQUEST_CODE = 68
    }

    private val route = kz.vozon.vozon.models.Route()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_new)
        supportActionBar!!.subtitle = "о свободной машине"

        v_transport.setOnClickListener{
            val i = Intent(this, kz.vozon.vozon.ui.courier.transport.TransportListActivity::class.java)
            startActivityForResult(i, TRANSPORT_REQUEST_CODE)
        }
        v_start_point.setOnClickListener {
            val i = Intent(this@RouteNewActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            startActivityForResult(i, START_POINT_REQUEST_CODE)
        }
        v_end_point.setOnClickListener {
            val i = Intent(this@RouteNewActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            startActivityForResult(i, END_POINT_REQUEST_CODE)
        }
        v_shipping_date.setOnClickListener(setDateListener(this))
        v_shipping_time.setOnClickListener(setTimeListener(this))
    }


    private fun create(){
        try{
            checkNotNull(route.transport){getString(R.string.enter_transport)}
            check(route.startPoint != null){"Укажите местонахождение транспорта"}
            route.shippingDate = v_shipping_date.get(getString(R.string.enter_date))
            route.shippingTime = v_shipping_time.get(getString(R.string.enter_time))
            route.comment = v_comment.get()

            Api.courierService.routesAdd(route).run3(this){
                setResult(Activity.RESULT_OK)
                finish()
            }

        }catch (ex: IllegalStateException){
            snack(ex.message ?: getString(R.string.something_went_wrong))
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
                TRANSPORT_REQUEST_CODE -> {
                    route.transport = data!!.get(kz.vozon.vozon.models.Transport::class.java)
                    v_transport.text = route.transport!!.fullName
                }
                START_POINT_REQUEST_CODE -> {
                    route.startPoint = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    v_start_point.content = route.startPoint!!.getShortName()
                }
                END_POINT_REQUEST_CODE -> {
                    route.endPoint = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    v_end_point.content = route.endPoint!!.getShortName()
                }
            }
        }
    }
}











