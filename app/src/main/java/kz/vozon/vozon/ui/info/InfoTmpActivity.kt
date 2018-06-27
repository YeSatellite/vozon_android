package kz.vozon.vozon.ui.info

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import kz.vozon.vozon.R
import kz.vozon.vozon.models.InfoTmp
import kz.vozon.vozon.ui.ListActivity
import kz.vozon.vozon.ui.ListFragment
import kz.vozon.vozon.utility.Shared
import kz.vozon.vozon.utility.put
import kz.vozon.vozon.utility.run2
import kz.vozon.vozon.utility.snack
import kotlinx.android.synthetic.main.item_info_tmp.view.*


class InfoTmpActivity: kz.vozon.vozon.ui.ListActivity<kz.vozon.vozon.models.InfoTmp, InfoTmpActivity.ViewHolder>(){
    var adapter: kz.vozon.vozon.ui.ListActivity<kz.vozon.vozon.models.InfoTmp, InfoTmpActivity.ViewHolder>.ListAdapter? = null
    var list: List<kz.vozon.vozon.models.InfoTmp> = ArrayList()

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        this.adapter = adapter
        Shared.call?.clone()?.run2(srRefresh,{ body ->
            list = body
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            snack(error)
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
        val i = Intent()
        i.put(item)
        setResult(Activity.RESULT_OK,i)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.m_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter!!.list = list.filter {
                    infoTmp -> infoTmp.name!!.contains(text!!,true)
                }
                adapter!!.notifyDataSetChanged()
                return true
            }

        })

        return true
    }
}










