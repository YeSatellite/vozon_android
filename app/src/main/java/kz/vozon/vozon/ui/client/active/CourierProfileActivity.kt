package kz.vozon.vozon.ui.client.active

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_courier_profile.*
import kz.vozon.vozon.R
import kz.vozon.vozon.models.User
import kz.vozon.vozon.utility.get
import kz.vozon.vozon.utility.src

class CourierProfileActivity : kz.vozon.vozon.ui.BackPressCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_courier_profile)

        val user = intent.get(kz.vozon.vozon.models.User::class.java)

        v_avatar.src(user.avatar,R.drawable.user_placeholder)
        v_transport.text = user.name
        v_courier_type.text = user.courierTypeName
        v_about.text = user.about
        v_city.text = user.city?.getShortName() ?: ""
        v_phone.text = user.phone
        v_experience.text = user.experienceStr()
        v_setting.visibility = View.GONE

    }
}
