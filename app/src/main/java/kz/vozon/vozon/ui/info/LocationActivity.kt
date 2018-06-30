package kz.vozon.vozon.ui.info

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import kotlinx.android.synthetic.main.item_info_tmp.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.models.InfoTmp
import kz.vozon.vozon.utility.*
import retrofit2.Call

class LocationActivity : AppCompatActivity() {
    companion object {
        const val EXTRA = "extra"
    }

    var state = 0

    val i = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        next(0)
    }

    fun next(id: Long) {
        val selectedFragment = when (state) {
            0 -> kz.vozon.vozon.ui.info.LocationFragment(Api.infoService.country())
            1 -> kz.vozon.vozon.ui.info.LocationFragment(Api.infoService.region(id))
            2 -> kz.vozon.vozon.ui.info.CityFragment(Api.infoService.city(id))
            3 -> {
                if (intent.getStringExtra(EXTRA) == EXTRA)
                    kz.vozon.vozon.ui.info.LocationExtraFragment()
                else {
                    setResult(Activity.RESULT_OK, i)
                    finish()
                    return
                }
            }
            4 -> {
                setResult(Activity.RESULT_OK, i)
                finish()
                return
            }
            else -> return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.v_container, selectedFragment)
        transaction.commit()
        state++
    }
}


class FilterLocationActivity : AppCompatActivity() {

    var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        intent.put(InfoTmp().apply { name = "Stranna" })
        next(false)
    }

    fun next(end: Boolean) {
        val location = intent.get(InfoTmp::class.java)
        supportActionBar!!.title = location.name
        if (end){
            val i = Intent()
            i.put(location)
            i.putExtra(Shared.type,when(state){
                2 -> Shared.Type.country
                3 -> Shared.Type.region
                4 -> Shared.Type.city
                else -> Shared.Type.city
            })
            setResult(Activity.RESULT_OK, i)

            finish()
            return
        }
        val selectedFragment = when (state) {
            0 -> FilterLocationFragment(Api.infoService.country())
            1 -> FilterLocationFragment(Api.infoService.region(location.id!!))
            2 -> FilterLocationFragment(Api.infoService.cityTmp(location.id!!))
            3 -> {
                state++
                next(true)
                return
            }
            else -> return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.v_container, selectedFragment)
        transaction.commit()
        state++
    }
}

@SuppressLint("ValidFragment")
class FilterLocationFragment(val call: Call<List<InfoTmp>>) : kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.InfoTmp, FilterLocationFragment.ViewHolder>() {
    var adapter: kz.vozon.vozon.ui.ListFragment<kz.vozon.vozon.models.InfoTmp, ViewHolder>.ListAdapter? = null
    var list: List<kz.vozon.vozon.models.InfoTmp> = ArrayList()


    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        call.clone().run2(srRefresh,{body ->
            this.adapter = adapter
            if(activity!!.intent.getBooleanExtra("have",false))
                list = mutableListOf(InfoTmp(-1,"All")).apply { addAll(body) }
            else {
                list = body
                activity!!.intent.putExtra("have", true)
            }
            adapter.list = list
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
        if (item.id == -1L)(activity as FilterLocationActivity).next(true)
        activity!!.intent.put(item)
        (activity as FilterLocationActivity).next(false)
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
                            infoTmp.name!!.contains(text!!, true) }
                    adapter!!.notifyDataSetChanged()
                    return true
                }
                return false
            }

        })
    }
}
