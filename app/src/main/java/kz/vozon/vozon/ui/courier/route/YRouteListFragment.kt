package kz.vozon.vozon.ui.courier.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.crashlytics.android.Crashlytics
import kz.vozon.vozon.R
import kz.vozon.vozon.models.Route
import kz.vozon.vozon.ui.ListFragment
import kz.vozon.vozon.ui.ListToolbarFragment
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.fragment_list_toolbar.view.*
import kotlinx.android.synthetic.main.item_courier_route.view.*


class YRouteListFragment : kz.vozon.vozon.ui.ListToolbarFragment<kz.vozon.vozon.models.Route, YRouteListFragment.ViewHolder>() {
    companion object {
        const val ROUTE_NEW_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.courierService.routes().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        v.v_toolbar.title = getString(R.string.ads)
        return v
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_courier_route, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hImage = v.v_image!!
        val hTransport = v.v_transport!!
        val hDate = v.v_date!!
        val hTType = v.v_t_type!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.Route) {
        holder.hImage.src(item.transport?.image1,R.drawable.tmp)
        holder.hTransport.text = item.transport?.fullName
        holder.hDate.text = item.shippingDate?.dateFormat()
        holder.hTType.text = getString(R.string._o_,item.transport?.type?.name,item.transport?.loadTypeName)
        holder.hStartPoint.text = item.startPoint?.getShortName()
        holder.hEndPoint.textIf = item.endPoint?.getShortName()

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
                val i = Intent(activity, kz.vozon.vozon.ui.courier.route.RouteNewActivity::class.java)
                startActivityForResult(i, ROUTE_NEW_ACTIVITY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ROUTE_NEW_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                refresh!!.run()
            }
        }
    }

    override fun onItemClick(item: kz.vozon.vozon.models.Route) {
        val i = Intent(activity, kz.vozon.vozon.ui.courier.route.YRouteDetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
