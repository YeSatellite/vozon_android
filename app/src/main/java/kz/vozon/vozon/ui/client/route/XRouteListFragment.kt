package kz.vozon.vozon.ui.client.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import kotlinx.android.synthetic.main.fragment_list_toolbar.view.*
import kotlinx.android.synthetic.main.item_client_route.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.*


class XRouteListFragment : kz.vozon.vozon.ui.ListToolbarFragment<kz.vozon.vozon.models.Route, XRouteListFragment.ViewHolder>() {
    companion object {
        const val ROUTE_FILTER_ACTIVITY = 25
    }

    private var filter = kz.vozon.vozon.models.Route.FilterRoute()

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.clientService.routes(
                filter.type,
                filter.startPoint?.id,
                filter.endPoint?.id,
                filter.startDate,
                filter.endDate
        ).run2(srRefresh,{body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            srRefresh.snack(error)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        v.v_toolbar.title = getString(R.string.ads)
        return v
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_client_route, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hImage = v.v_image!!
        val hTransport = v.v_transport!!
        val hTType = v.v_t_type!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hAvatar = v.v_avatar!!
        val hName = v.v_name!!
        val hRating = v.v_rating!!
        val hDate = v.v_date!!


    }

    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.Route) {
        holder.hImage.src(item.transport?.image1,R.drawable.tmp)
        holder.hTransport.text = item.transport?.fullName
        holder.hTType.text = getString(R.string._o_,item .transport?.type?.name, item.transport?.loadTypeName)
        holder.hStartPoint.text = item.startPoint?.getShortName()
        holder.hEndPoint.textIf = item.endPoint?.getShortName()
        holder.hName.text =item.owner?.name
        holder.hAvatar.src(item.owner?.avatar,R.drawable.user_placeholder)
        holder.hRating.text =item.owner?.rating
        holder.hDate.text = item.shippingDate?.dateFormat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                val i = Intent(activity, kz.vozon.vozon.ui.client.route.RouteFilterActivity::class.java)
                i.put(filter)
                startActivityForResult(i, ROUTE_FILTER_ACTIVITY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ROUTE_FILTER_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                filter = data!!.get(kz.vozon.vozon.models.Route.FilterRoute::class.java)
                refresh!!.run()
            }
        }
    }

    override fun onItemClick(item: kz.vozon.vozon.models.Route) {
        val i = Intent(activity, kz.vozon.vozon.ui.client.route.XRouteDetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
