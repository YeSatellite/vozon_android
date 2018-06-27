package kz.vozon.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Country() : Serializable {

    constructor(name: String,phoneCode: String) : this() {
        this.name = name
        this.phoneCode = phoneCode
    }

    @Expose var id: Long? = null

    @Expose var name: String? = null

    @SerializedName("phone_code")
    @Expose var phoneCode: String? = null

    @SerializedName("phone_mask")
    @Expose var phoneMask: String? = null
}