package kz.vozon.vozon.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import kz.vozon.vozon.utility.Shared
import kz.vozon.vozon.utility.norm


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        Shared.token = FirebaseInstanceId.getInstance().token ?: ""
        norm(Shared.token)
    }
}
