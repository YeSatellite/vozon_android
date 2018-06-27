package kz.vozon.vozon.ui.courier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_courier_main.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.Shared

class YMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Shared.setTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courier_main)
        v_navigation.itemIconTintList = null
        v_navigation.enableAnimation(false)
        v_navigation.enableShiftingMode(false)
        v_navigation.enableItemShiftingMode(false)
        v_navigation.setTextVisibility(false)

        v_navigation.setOnNavigationItemSelectedListener {item ->
            val selectedFragment = when (item.itemId) {
                R.id.m_route -> kz.vozon.vozon.ui.courier.route.YRouteListFragment()
                R.id.m_transport -> kz.vozon.vozon.ui.courier.transport.TransportListFragment()
                R.id.m_order -> kz.vozon.vozon.ui.courier.order.YOrderFragment()
                R.id.m_profile -> kz.vozon.vozon.ui.courier.YProfileFragment()
                else -> kz.vozon.vozon.ui.courier.YProfileFragment()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.v_container, selectedFragment)
            transaction.commit()
            true}

        v_navigation.selectedItemId = when(intent.getStringExtra(Shared.action)){
            Shared.Action.newOrder,
            Shared.Action.acceptOffer,
            Shared.Action.done -> R.id.m_order
            else -> R.id.m_route
        }
    }
}
