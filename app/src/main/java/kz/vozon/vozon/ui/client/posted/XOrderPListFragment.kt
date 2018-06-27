package kz.vozon.vozon.ui.client.posted

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_client_order.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.client.TTypeActivity
import kz.vozon.vozon.utility.*


class XOrderPListFragment : kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.Order, XOrderPListFragment.ViewHolder>() {
    companion object {
        const val OFFER_LIST_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.clientService.orders(Shared.posted).run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_client_order, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hTitle = v.v_title!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hPosition = v.v_t_type!!
        val hShowOffers = v.v_show_offers!!
        val hImage= v.v_image!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.Order) {
        holder.hTitle.text = item.title
        holder.hStartPoint.text = item.startPoint!!.getShortName()
        holder.hEndPoint.text = item.endPoint!!.getShortName()
        holder.hPosition.text = item.startPoint!! - item.endPoint!!
        holder.hImage.src(if(item.image1 != null) item.image1 else item.image2,R.drawable.tmp)
        holder.hShowOffers.setOnClickListener {
            val i = Intent(activity, kz.vozon.vozon.ui.client.posted.XOfferListActivity::class.java)
            i.put(item)
            startActivityForResult(i,OFFER_LIST_ACTIVITY)
        }
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
        val i = Intent(activity, kz.vozon.vozon.ui.client.posted.XOrderPDetailActivity::class.java)
        i.put(item)
        startActivityForResult(i, OFFER_LIST_ACTIVITY)
    }
}
