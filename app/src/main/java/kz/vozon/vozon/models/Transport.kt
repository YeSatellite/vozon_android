package kz.vozon.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by yesat on 09/03/18.
 */

class Transport : Serializable {
    @Expose var id: Long? = null

    @Expose var owner: kz.vozon.vozon.models.User? = null

    @Expose var model: Long? = null
    @SerializedName("model_name")
    @Expose var modelName: String? = null

    var mark: Long? = null
    @SerializedName("mark_name")
    @Expose var markName: String? = null

    @Expose var type: kz.vozon.vozon.models.TType? = null
    @SerializedName("type_id")
    @Expose var typeId: Long? = null

    @SerializedName("load_type")
    @Expose var loadType: Long? = null
    @SerializedName("load_type_name")
    @Expose var loadTypeName: String? = null

    @Expose var image1: String? = null
    @Expose var image2: String? = null

    @Expose var number: String? = null

    @Expose var width: Float? = null

    @Expose var height: Float? = null

    @Expose var length: Float? = null

    @Expose var comment: String? = null

    val fullName: String get() = "$markName, $modelName"

}
