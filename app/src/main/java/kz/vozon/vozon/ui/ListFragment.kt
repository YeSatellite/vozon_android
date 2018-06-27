package kz.vozon.vozon.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.view.*
import kz.vozon.vozon.R

abstract class ListFragment<T,V : ListFragment.ViewHolder> : Fragment() {
    var refresh: Runnable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = ListAdapter()
        v.v_list.adapter = adapter
        val srRefresh = v.v_refresh

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
        return v
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
