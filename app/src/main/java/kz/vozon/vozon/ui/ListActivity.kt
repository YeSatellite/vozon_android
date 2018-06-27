package kz.vozon.vozon.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.Shared

abstract class ListActivity<T,V : kz.vozon.vozon.ui.ListFragment.ViewHolder>: kz.vozon.vozon.ui.BackPressCompatActivity() {
    var refresh: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list)

        val title = intent.getStringExtra("title")
        if (title != null)supportActionBar?.title = title

        val adapter = ListAdapter()
        v_list.adapter = adapter
        val srRefresh = v_refresh

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            refreshListener(adapter,srRefresh)
        }


        srRefresh.setOnRefreshListener(refreshListener)

        refresh = Runnable {
            refreshListener.onRefresh()
            srRefresh.isRefreshing = true
        }

        srRefresh.post(refresh)
        srRefresh.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE)
    }

    inner class ListAdapter: RecyclerView.Adapter<V>() {
        var list: List<T> = java.util.ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
            return onCreateViewHolder2(parent)
        }

        override fun onBindViewHolder(holder: V, position: Int) {
            onBindViewHolder2(holder,list[position])

            holder.v.setOnClickListener {
                onItemClick(list[position])
            }
        }

        override fun getItemCount() = list.size
    }

    abstract class ViewHolder(var v: View) : RecyclerView.ViewHolder(v)

    abstract fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout)
    abstract fun onCreateViewHolder2(parent: ViewGroup): V
    abstract fun onBindViewHolder2(holder: V,item : T)
    open fun onItemClick(item: T){}
}
