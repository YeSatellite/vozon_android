package kz.vozon.vozon.ui.client.posted

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.ListFragment
import kz.vozon.vozon.models.Offer
import kz.vozon.vozon.models.Order
import kz.vozon.vozon.ui.BackPressCompatActivity
import kz.vozon.vozon.ui.client.active.CourierProfileActivity
import kz.vozon.vozon.ui.courier.transport.TransportDetailActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.item_offer.view.*

class XOfferListActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_list)

        supportActionBar!!.subtitle = "от водителей"

    }
}
class XOfferListFragment : kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.Offer, XOfferListFragment.ViewHolder>() {
    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        val order = activity!!.intent.get(kz.vozon.vozon.models.Order::class.java)
        Api.clientService.offers(order.id!!).run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_offer, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hAvatar = v.v_avatar!!
        val hName = v.v_name!!
        val hRating = v.v_rating!!
        val hDate = v.v_date!!
        val hAbout = v.v_about!!
        val hTName = v.v_t_name!!
        val hTType = v.v_t_type!!
        val hPrice = v.v_price!!
        val hAccept = v.v_accept!!
        val hReject = v.v_reject!!
        val hCourier = v.v_courier!!
        val hTransport = v.v_transport!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.Offer) {
        val user = item.transport?.owner!!
        holder.hAvatar.src(user.avatar,R.drawable.user_placeholder)
        holder.hName.text = user.name
        holder.hRating.text = if(user.rating.isNullOrEmpty()) user.courierTypeName
        else getString(R.string._o_,user.courierTypeName,user.rating)
        holder.hDate.text = item.created!!.dateFormat()
        holder.hAbout.text = item.transport?.owner?.about
        if (holder.hAbout.text.isNullOrEmpty()) holder.hAbout.visibility = View.GONE
        holder.hTName.text = item.transport?.modelName
        holder.hTType.text = item.paymentTypeName
        holder.hPrice.text = getString(R.string._s_, item.price.toString(),item.currency)

        holder.hAccept.setOnClickListener {
            Api.clientService.offersAccept(item.order!!, item.id!!)
                    .run2(activity!!,{
                        activity!!.setResult(Activity.RESULT_OK, Intent())
                        activity!!.finish()
                    },{
                        _, error ->
                        activity!!.snack(error)
                    })
        }
        holder.hReject.setOnClickListener{
            val map = hashMapOf("offer" to item.id!!)
            Api.clientService.offersReject(item.order!!, map)
                    .run2(activity!!,{
                    },{
                        _, error ->
                        activity!!.snack(error)
                    })
        }

        holder.hCourier.setOnClickListener {
            val i =Intent(activity, kz.vozon.vozon.ui.client.active.CourierProfileActivity::class.java)
            i.put(item.transport!!.owner!!)
            startActivity(i)
        }
        holder.hTransport.setOnClickListener {
            val i =Intent(activity, kz.vozon.vozon.ui.courier.transport.TransportDetailActivity::class.java)
            i.putExtra("have",false)
            i.put(item.transport!!)
            startActivity(i)
        }

    }
}