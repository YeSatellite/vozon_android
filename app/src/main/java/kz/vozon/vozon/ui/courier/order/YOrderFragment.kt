package kz.vozon.vozon.ui.courier.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.Shared
import kotlinx.android.synthetic.main.tmp_pager.view.*


class YOrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.tmp_pager, container, false)

        val typedValue = TypedValue()
        context!!.theme.resolveAttribute(R.attr.textColorLarge, typedValue, true)
        val color = typedValue.data

        v.v_toolbar.setTitleTextColor(color)
        v.v_toolbar.setSubtitleTextColor(color)
        v.v_toolbar.title = getString(R.string.orders)
        (activity as AppCompatActivity).setSupportActionBar(v.v_toolbar)

        v.pager.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            private var tabs = arrayOf("Новые", "Ожидание", "Активные")
            override fun getPageTitle(position: Int) = tabs[position]
            override fun getCount() = 3
            override fun getItem(position: Int): Fragment? {
                return when (position) {
                    0 -> kz.vozon.vozon.ui.courier.order.YOrderPList0Fragment()
                    1 -> kz.vozon.vozon.ui.courier.order.YOrderWListFragment()
                    2 -> kz.vozon.vozon.ui.courier.order.YOrderAListFragment()
                    else -> null
                }
            }
        }
        v.pager.currentItem = when(activity!!.intent.getStringExtra(Shared.action)){
            Shared.Action.acceptOffer,
            Shared.Action.done -> {
                activity!!.intent.removeExtra(Shared.action)
                2
            }
            else -> 0
        }

        v.v_tab_layout.setupWithViewPager(v.pager)

        return v
    }


}
