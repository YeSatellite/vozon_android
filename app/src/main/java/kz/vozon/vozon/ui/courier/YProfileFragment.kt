package kz.vozon.vozon.ui.courier

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.ui.client.XSettingActivity
import kz.vozon.vozon.utility.*
import kotlinx.android.synthetic.main.fragment_courier_profile.view.*

class YProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_courier_profile, container, false)

        val user = Shared.currentUser

        v.v_avatar.src(user.avatar,R.drawable.user_placeholder)
        v.v_transport.text = user.name
        v.v_courier_type.text = user.courierTypeName
        v.v_about.text = user.about
        v.v_city.text = user.city?.getShortName() ?: ""
        v.v_phone.text = user.phone
        v.v_experience.text = user.experienceStr()
        v.v_setting.setOnClickListener{
            startActivityForResult(Intent(context, kz.vozon.vozon.ui.courier.YSettingActivity::class.java),26)
        }
        v.v_setting.addFilter(R.attr.textColorLarge)
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 26){
            if (resultCode == Activity.RESULT_OK){
                activity?.finish()
            }
        }
    }
}
