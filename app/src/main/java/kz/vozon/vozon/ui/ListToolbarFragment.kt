package kz.vozon.vozon.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_list_toolbar.view.*
import kz.vozon.vozon.R

abstract class ListToolbarFragment<T,V : kz.vozon.vozon.ui.ListFragment.ViewHolder> : kz.vozon.vozon.ui.ListFragment<T, V>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_list_toolbar, container, false)
        (activity as AppCompatActivity).setSupportActionBar(v.v_toolbar)

        val typedValue = TypedValue()
        context!!.theme.resolveAttribute(R.attr.textColorLarge, typedValue, true)
        val color = typedValue.data
        v.v_toolbar.setTitleTextColor(color)
        v.v_toolbar.setSubtitleTextColor(color)

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
}
