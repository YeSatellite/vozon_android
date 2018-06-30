package kz.vozon.vozon.ui.courier.order

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import kotlinx.android.synthetic.main.item_info_tmp_r.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.models.InfoTmp
import kz.vozon.vozon.ui.info.FilterLocationActivity
import kz.vozon.vozon.utility.Shared
import kz.vozon.vozon.utility.get
import kz.vozon.vozon.utility.norm


class OrderFilterActivity: kz.vozon.vozon.ui.ListActivity<InfoTmp, OrderFilterActivity.ViewHolder>(){

    companion object {
        const val CITY_REQUEST_CODE = 85
    }


    var adapter: kz.vozon.vozon.ui.ListActivity<InfoTmp, OrderFilterActivity.ViewHolder>.ListAdapter? = null

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        this.adapter = adapter
        adapter.list = Shared.Filter.list()
        adapter.notifyDataSetChanged()
        srRefresh.post{
            srRefresh.isRefreshing= false
        }
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_tmp_r, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hName = v.v_transport!!
        val hRemove = v.v_remove!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: InfoTmp) {
        holder.hName.text = item.name
        holder.hRemove.setOnClickListener{
            Shared.Filter.remove(item)
            refresh!!.run()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                val i = Intent(this, FilterLocationActivity::class.java)
                startActivityForResult(i, CITY_REQUEST_CODE)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CITY_REQUEST_CODE -> {
                    val location = data!!.get(InfoTmp::class.java)
                    Shared.Filter.add(location,data.getStringExtra(Shared.type))
                    norm(data.getStringExtra(Shared.type))
                    refresh!!.run()
                }
            }
        }
    }
}










