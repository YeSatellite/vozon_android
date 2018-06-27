package kz.vozon.vozon.ui.courier.order


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_order_list.view.*
import kz.vozon.vozon.R


class YOrderPList0Fragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_order_list, container, false)
        v.v_city_add.setOnClickListener{
            val i = Intent(activity, kz.vozon.vozon.ui.courier.order.OrderFilterActivity::class.java)
            startActivity(i)
        }
        return v
    }

}
