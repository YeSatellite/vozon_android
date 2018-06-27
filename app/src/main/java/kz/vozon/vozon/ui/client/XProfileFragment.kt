package kz.vozon.vozon.ui.client


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.Shared
import kz.vozon.vozon.utility.src
import kotlinx.android.synthetic.main.fragment_client_profile.view.*

class XProfileFragment : Fragment() {
    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v =  inflater.inflate(R.layout.fragment_client_profile, container, false)

        val user = Shared.currentUser

        v?.v_avatar?.src(user.avatar,R.drawable.user_placeholder)
        v?.v_transport?.text = user.name
        v?.v_about?.text = user.about
        v?.v_city?.text = user.city?.getShortName() ?: ""
        v?.v_phone?.text = user.phone

        v?.v_setting?.setOnClickListener{
            startActivityForResult(Intent(context, kz.vozon.vozon.ui.client.XSettingActivity::class.java),26)
        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val user = Shared.currentUser

        v?.v_avatar?.src(user.avatar,R.drawable.user_placeholder)
        v?.v_transport?.text = user.name
        v?.v_about?.text = user.about
        v?.v_city?.text = user.city?.getShortName() ?: ""
        v?.v_phone?.text = user.phone

        if(requestCode == 26){
            if (resultCode == Activity.RESULT_OK){
                activity?.finish()
            }
        }
    }
}
