package kz.vozon.vozon.ui.client

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_t_type.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.*


class TTypeActivity: kz.vozon.vozon.ui.ListActivity<kz.vozon.vozon.models.TType, TTypeActivity.ViewHolder>() {

    companion object {
        const val ORDER_NEW_ACTIVITY = 36
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.infoService.tType().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_t_type, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hIcon = v.v_icon!!
        val hName = v.v_transport!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.TType) {
        holder.hIcon.src(item.icon,R.drawable.tmp_truck)
        holder.hName.text = item.name
    }

    override fun onItemClick(item: kz.vozon.vozon.models.TType){
        val i = Intent(this, kz.vozon.vozon.ui.client.OrderNewActivity::class.java)
        i.put(item)
        startActivityForResult(i,ORDER_NEW_ACTIVITY)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ORDER_NEW_ACTIVITY){
            if (resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK, Intent())
                finish()
            }
        }
    }
}
