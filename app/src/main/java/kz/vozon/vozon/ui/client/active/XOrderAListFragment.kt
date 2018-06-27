package kz.vozon.vozon.ui.client.active

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import kz.vozon.vozon.ui.ListFragment
import kz.vozon.vozon.R
import kz.vozon.vozon.models.Order
import kz.vozon.vozon.ui.client.TTypeActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_active_order.view.*


class XOrderAListFragment : kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.Order, XOrderAListFragment.ViewHolder>() {
    companion object {
        const val OFFER_LIST_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.clientService.orders(Shared.active).run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_active_order, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hTitle = v.v_title!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hPosition = v.v_t_type!!
        val hYourCourier = v.v_your_courier!!
        val hImage= v.v_image!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.Order) {
        holder.hTitle.text = item.title
        holder.hStartPoint.text = item.startPoint!!.getShortName()
        holder.hEndPoint.text = item.endPoint!!.getShortName()
        holder.hPosition.text = item.startPoint!! - item.endPoint!!
        holder.hImage.src(if(item.image1 != null) item.image1 else item.image2,R.drawable.tmp)
        holder.hYourCourier.setOnClickListener({
            val i = Intent(activity, kz.vozon.vozon.ui.client.active.CourierProfileActivity::class.java)
            i.put(item.offer!!.transport!!.owner!!)
            startActivity(i)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                val i = Intent(activity, kz.vozon.vozon.ui.client.TTypeActivity::class.java)
                startActivityForResult(i,32)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OFFER_LIST_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                refresh!!.run()
            }
        }
    }

    override fun onItemClick(item: kz.vozon.vozon.models.Order) {
        val i = Intent(activity, kz.vozon.vozon.ui.client.active.XOrderADetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
