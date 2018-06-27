package kz.vozon.vozon.ui.client.route

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_route_filter.*
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.info.LocationActivity
import kz.vozon.vozon.utility.*
import java.util.*

class RouteFilterActivity: kz.vozon.vozon.ui.BackPressCompatActivity() {

    companion object {
        const val START_POINT_REQUEST_CODE = 54
        const val END_POINT_REQUEST_CODE = 57
        const val TYPE_REQUEST_CODE = 68
    }

    private var filter : kz.vozon.vozon.models.Route.FilterRoute? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_filter)

        filter = intent.get(kz.vozon.vozon.models.Route.FilterRoute::class.java)

        v_start_point.setOnClickListener {
            val i = Intent(this@RouteFilterActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            startActivityForResult(i, START_POINT_REQUEST_CODE)
        }
        v_end_point.setOnClickListener {
            val i = Intent(this@RouteFilterActivity, kz.vozon.vozon.ui.info.LocationActivity::class.java)
            startActivityForResult(i, END_POINT_REQUEST_CODE)
        }
        v_start_date.setOnClickListener{
            val calendar = Calendar.getInstance()
            DatePickerDialog(this@RouteFilterActivity,
                    DatePickerDialog.OnDateSetListener {
                        _, year, month, dayOfMonth ->
                        v_start_date.content = "$year-$month-$dayOfMonth"
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        v_end_date.setOnClickListener{
            val calendar = Calendar.getInstance()
            DatePickerDialog(this@RouteFilterActivity,
                    DatePickerDialog.OnDateSetListener {
                        _, year, month, dayOfMonth ->
                        v_end_date.content = "$year-$month-$dayOfMonth"
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        v_type.setOnClickListener {
            val i = Intent(this@RouteFilterActivity, kz.vozon.vozon.ui.info.MultiInfoActivity::class.java)
            i.putExtra(Shared.title,getString(R.string.t_type))
            Shared.call = Api.infoService.tTypeTmp()
            startActivityForResult(i,TYPE_REQUEST_CODE)
        }
        fill()

    }

    private fun fill(){
        v_type.content = filter!!.typeNames
        v_start_point.content = filter!!.startPoint?.getShortName()
        v_end_point.content = filter!!.endPoint?.getShortName()
        v_start_date.content = filter!!.startDate
        v_end_date.content = filter!!.endDate
    }


    private fun filter(){
        try{
            filter!!.startDate = v_start_date.get()
            filter!!.endDate = v_end_date.get()

            val i = Intent()
            i.put(filter!!)
            setResult(Activity.RESULT_OK,i)
            finish()


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
                filter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                START_POINT_REQUEST_CODE -> {
                    filter!!.startPoint = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    v_start_point.content = filter!!.startPoint!!.getShortName()
                }
                END_POINT_REQUEST_CODE -> {
                    filter!!.endPoint = data!!.get(kz.vozon.vozon.models.Location::class.java)
                    v_end_point.content = filter!!.endPoint!!.getShortName()
                }
                TYPE_REQUEST_CODE -> {
                    val filtered = Shared.list.filter { multiInfo -> multiInfo.isChecked }
                    filter!!.type = filtered.joinToString(separator = ","){it.id.toString()}
                    filter!!.typeNames = filtered.joinToString(separator = ","){it.name.toString()}
                    v_type.content = filter!!.typeNames
                }
            }
        }
    }
}











