package kz.vozon.vozon.ui.client


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tmp_pager.view.*
import kz.vozon.vozon.R

class XOrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.tmp_pager, container, false)

        v.v_toolbar.title = getString(R.string.orders)
        (activity as AppCompatActivity).setSupportActionBar(v.v_toolbar)

        v.pager.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            private var tabs = arrayOf("Новые", "Активные")
            override fun getPageTitle(position: Int) = tabs[position]
            override fun getCount() = tabs.size
            override fun getItem(position: Int): Fragment? {
                return when (position) {
                    0 -> kz.vozon.vozon.ui.client.posted.XOrderPListFragment()
                    1 -> kz.vozon.vozon.ui.client.active.XOrderAListFragment()
                    else -> null
                }
            }
        }
        v.v_tab_layout.setupWithViewPager(v.pager)

        return v
    }
}
