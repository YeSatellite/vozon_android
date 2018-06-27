package kz.vozon.vozon.ui.courier.transport

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.vozon.vozon.ui.ListFragment
import kz.vozon.vozon.R
import kz.vozon.vozon.models.Transport
import kz.vozon.vozon.ui.ListActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.item_transport.view.*


class TransportListActivity: kz.vozon.vozon.ui.ListActivity<kz.vozon.vozon.models.Transport, TransportListActivity.ViewHolder>() {

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.courierService.transports().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transport, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        //        val hImage = v.v_image!!
        val hName = v.v_transport!!
        val hNumber= v.v_number!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.Transport) {
        holder.hName.text = item.fullName
        holder.hNumber.text = item.number
    }

    override fun onItemClick(item: kz.vozon.vozon.models.Transport){
        val i = Intent()
        i.put(item)
        setResult(Activity.RESULT_OK,i)
        finish()
    }
}










