package kz.vozon.vozon.ui.info

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_multi_info.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.Shared
import kz.vozon.vozon.utility.run2
import kz.vozon.vozon.utility.snack

class MultiInfoActivity: kz.vozon.vozon.ui.BackPressCompatActivity() {
    var adapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list)

        adapter = ListAdapter()
        v_list.adapter = adapter
        v_refresh.isEnabled = false

        title = intent.getStringExtra(Shared.title)

        refreshListener()
    }
    inner class ListAdapter: RecyclerView.Adapter<MultiInfoActivity.ViewHolder>() {
        var listFilter: List<kz.vozon.vozon.models.MultiInfo> = java.util.ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiInfoActivity.ViewHolder {
            val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_multi_info, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: MultiInfoActivity.ViewHolder, position: Int) {
            holder.hName.text = listFilter[position].name
            holder.hName.isChecked = listFilter[position].isChecked
            holder.hName.setOnClickListener {
                listFilter[position].isChecked = listFilter[position].isChecked.not()
                holder.hName.isChecked = listFilter[position].isChecked
            }
        }

        override fun getItemCount() = listFilter.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val hName = v.v_transport!!

    }

    fun refreshListener() {
        if (Shared.list.isEmpty())
            Shared.call?.clone()?.run2(v_refresh,{ body ->
                Shared.list = body.map { infoTmp -> kz.vozon.vozon.models.MultiInfo.toMultiInfo(infoTmp)}
                adapter?.listFilter = Shared.list
                adapter?.notifyDataSetChanged()
            },{ _, error ->
                snack(error)
            })
        else {
            adapter?.listFilter = Shared.list
            adapter?.notifyDataSetChanged()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_done, menu)

        val searchView = menu.findItem(R.id.m_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter!!.listFilter = Shared.list.filter {
                    MultiInfo -> MultiInfo.name!!.contains(text!!,true)
                }
                adapter!!.notifyDataSetChanged()
                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                setResult(Activity.RESULT_OK)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}







