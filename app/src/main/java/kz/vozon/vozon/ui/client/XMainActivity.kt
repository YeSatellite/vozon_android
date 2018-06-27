package kz.vozon.vozon.ui.client

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.client.route.XRouteListFragment
import kz.vozon.vozon.utility.Shared
import kotlinx.android.synthetic.main.activity_client_main.*

class XMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Shared.setTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_main)
        v_navigation.itemIconTintList = null
        v_navigation.enableAnimation(false)
        v_navigation.enableShiftingMode(false)
        v_navigation.enableItemShiftingMode(false)
        v_navigation.setTextVisibility(false)

        v_navigation.setOnNavigationItemSelectedListener {item ->
            val selectedFragment = when (item.itemId) {
                R.id.m_route -> kz.vozon.vozon.ui.client.route.XRouteListFragment()
                R.id.m_order -> kz.vozon.vozon.ui.client.XOrderFragment()
                R.id.m_profile -> kz.vozon.vozon.ui.client.XProfileFragment()
                else -> kz.vozon.vozon.ui.client.XProfileFragment()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.v_container, selectedFragment)
            transaction.commit()
            true}

        v_navigation.selectedItemId = when(intent.getStringExtra(Shared.action)){
            Shared.Action.responseOrder -> R.id.m_order
            else -> R.id.m_route
        }
    }
}
