package kz.vozon.vozon.ui.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.view.*
import kz.vozon.vozon.ui.ListFragment
import kz.vozon.vozon.R
import kz.vozon.vozon.models.InfoTmp
import kz.vozon.vozon.utility.run2
import kz.vozon.vozon.utility.snack
import kotlinx.android.synthetic.main.item_info_tmp.view.*
import retrofit2.Call


@SuppressLint("ValidFragment")
class LocationFragment(val call: Call<List<kz.vozon.vozon.models.InfoTmp>>) : kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.InfoTmp, LocationFragment.ViewHolder>() {
    var adapter: kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.InfoTmp, LocationFragment.ViewHolder>.ListAdapter? = null
    var list: List<kz.vozon.vozon.models.InfoTmp> = ArrayList()


    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        call.clone().run2(srRefresh,{body ->
            this.adapter = adapter
            list = body

            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_tmp, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : kz.vozon.vozon.ui.ListFragment.ViewHolder(v){
        val hName = v.v_transport!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: kz.vozon.vozon.models.InfoTmp) {
        holder.hName.text = item.name
    }

    override fun onItemClick(item: kz.vozon.vozon.models.InfoTmp){
        (activity as kz.vozon.vozon.ui.info.LocationActivity).supportActionBar!!.title = item.name
        (activity as kz.vozon.vozon.ui.info.LocationActivity).next(item.id!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.m_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (adapter != null){
                    adapter!!.list = list.filter { infoTmp ->
                        infoTmp.name!!.contains(text!!, true)
                    }
                    adapter!!.notifyDataSetChanged()
                    return true
                }
                return false
            }

        })
    }
}
