@file:Suppress("DEPRECATION")

package kz.vozon.vozon.utility

import android.app.Activity
import android.app.ProgressDialog
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File


object Api {
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://188.166.50.157:8000/")
            .addConverterFactory((GsonConverterFactory.create(GsonBuilder().serializeNulls().create())))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request().newBuilder()
                if (getToken().length>10)
                    request = request.addHeader("Authorization", getToken())
                chain.proceed(request.build())
            }.build())
            .build()


    var authService = retrofit.create(AuthService::class.java)!!
    interface AuthService {
        companion object {
            const val path = "auth"
        }

        @Multipart
        @POST("$path/register/")
        fun register(
                @Part("phone") phone:RequestBody,
                @Part("name") name:RequestBody,
                @Part("city") city:RequestBody,
                @Part("about") about:RequestBody,
                @Part("type") type:RequestBody,
                @Part image: MultipartBody.Part?,
                @Part("courier_type") courier_type:RequestBody? =null,
                @Part("experience") experience:RequestBody? = null
        ): Call<Any>

        @POST("$path/login/")
        fun login(@Body phone_sms: Map<String, String>): Call<kz.vozon.vozon.models.User>

        @POST("$path/sent-sms/")
        fun sentSms(@Body phone: Map<String, String>): Call<Any>

        @DELETE("$path/remove-user/")
        fun remove_user(): Call<Any>
    }

    var infoService = retrofit.create(InfoService::class.java)!!
    interface InfoService {
        companion object {
            const val path = "info"
            const val transport = "transport"
        }

        @GET("$path/city/")
        fun city(@Query("region") regionId: Long): Call<List<kz.vozon.vozon.models.Location>>

        @GET("$path/region/")
        fun region(@Query("country") countryId: Long): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/country/")
        fun country(): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/country-phone/")
        fun countryPhone(): Call<List<kz.vozon.vozon.models.Country>>

        @GET("$path/payment-type/")
        fun paymentType(): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/other-type/")
        fun otherType(): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/$transport/type/")
        fun tType(): Call<List<kz.vozon.vozon.models.TType>>

        @GET("$path/$transport/type/")
        fun tTypeTmp(): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/$transport/load-type/")
        fun tLoadType(): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/$transport/mark/")
        fun tMark(): Call<List<kz.vozon.vozon.models.InfoTmp>>

        @GET("$path/$transport/model/")
        fun tModel(@Query("mark/") markId: Long): Call<List<kz.vozon.vozon.models.InfoTmp>>
    }

    var clientService = retrofit.create(ClientService::class.java)!!
    interface ClientService {
        companion object {
            const val path = "client"
        }

        @Multipart
        @PATCH("$path/clients/current/")
        fun profileUpdate(
                @Part("name") name:RequestBody,
                @Part("city_id") city:RequestBody,
                @Part("about") about:RequestBody,
                @Part image: MultipartBody.Part?
        ): Call<kz.vozon.vozon.models.User>


        @GET("$path/clients/current")
        fun test(): Call<kz.vozon.vozon.models.User>

        @GET("$path/order/")
        fun orders(@Query("status") status: String): Call<List<kz.vozon.vozon.models.Order>>

        @POST("$path/order/")
        fun orderAdd(@Body order: kz.vozon.vozon.models.Order): Call<kz.vozon.vozon.models.Order>

        @POST("$path/order/{id}/done/")
        @Multipart
        fun orderDone(@Path("id") id: Long,
                      @Part("rating") rating: Int): Call<Any>


        @DELETE("$path/order/{id}/")
        fun orderDelete(@Path("id") id: Long): Call<Any>


        @Multipart
        @PATCH("$path/order/{id}/")
        fun orderUpdate(@Path("id") id: Long,
                        @Part image1: MultipartBody.Part?,
                        @Part image2: MultipartBody.Part?): Call<kz.vozon.vozon.models.Order>

        @GET("$path/order/{id}/offers/")
        fun offers(@Path("id") orderId: Long): Call<List<kz.vozon.vozon.models.Offer>>

        @Multipart
        @POST("$path/order/{id}/offers/")
        fun offersAccept(@Path("id") orderId: Long,
                         @Part("offer") offerId: Long): Call<Any>

        @HTTP(method = "DELETE", path = "$path/order/{id}/offers/", hasBody = true)
        fun offersReject(@Path("id") orderId: Long,
                         @Body offer: Map<String, Long>): Call<Any>

        @GET("$path/routes/")
        fun routes(@Query("type") type: String?,
                   @Query("start_point") startPoint: Long?,
                   @Query("end_point") endPoint: Long?,
                   @Query("start_date") startDate: String?,
                   @Query("end_date") endDate: String?): Call<List<kz.vozon.vozon.models.Route>>
    }

    var courierService = retrofit.create(CourierService::class.java)!!
    interface CourierService {
        companion object {
            const val path = "courier"
        }

        @Multipart
        @PATCH("$path/couriers/current/")
        fun profileUpdate(
                @Part("name") name:RequestBody,
                @Part("city_id") city:RequestBody,
                @Part("about") about:RequestBody,
                @Part image: MultipartBody.Part?,
                @Part("courier_type") courier_type:RequestBody? =null,
                @Part("experience") experience:RequestBody? = null
        ): Call<kz.vozon.vozon.models.User>


        @GET("$path/couriers/current")
        fun test(): Call<kz.vozon.vozon.models.User>

        @GET("$path/transports/")
        fun transports(): Call<List<kz.vozon.vozon.models.Transport>>
        @POST("$path/transports/")
        fun transportsAdd(@Body transport: kz.vozon.vozon.models.Transport): Call<kz.vozon.vozon.models.Transport>
        @DELETE("$path/transports/{id}/")
        fun transportDelete(@Path("id") id: Long): Call<Any>
        @Multipart
        @PATCH("$path/transports/{id}/")
        fun transportsUpdate(@Path("id") id: Long,
                        @Part image1: MultipartBody.Part?,
                        @Part image2: MultipartBody.Part?): Call<kz.vozon.vozon.models.Transport>


        @GET("$path/order/")
        fun orders(@Query("status") status: String,
                   @Query("start_point") filter: String? = null): Call<List<kz.vozon.vozon.models.Order>>

        @POST("$path/order/{id}/offer/")
        fun offerAdd(@Path("id") orderId: Long,@Body offer: kz.vozon.vozon.models.Offer): Call<kz.vozon.vozon.models.Offer>

        @GET("$path/routes/")
        fun routes(): Call<List<kz.vozon.vozon.models.Route>>

        @POST("$path/routes/")
        fun routesAdd(@Body route: kz.vozon.vozon.models.Route): Call<kz.vozon.vozon.models.Route>

    }





    private fun getToken(): String {
        return "JWT ${Shared.currentUser.token}"
    }

    // ----------TEST-END----------
    fun test(run: () -> Unit) {
        Handler().postDelayed(run, 1000)
    }

    // ---------TEST-START---------

    val ERROR_CODE : Map<Int,String> = mapOf(
            100 to "Что то пошло не так",
            101 to "Этот номер уже зарегистрирован",
            102 to "Номер не зарегистрирован",
            103 to "Не правильный код",
            104 to "Данный номер не существует"
    )

}

fun <T> Call<T>.run3(context: Activity, ok: (body:T) -> Unit){
    this.run2(context,
            { body -> ok(body)},
            { _, error -> context.snack(error)}
    )
}

fun <T> Call<T>.run2(context: Activity,
                     ok: (body:T) -> Unit,
                     fail: (code: Int, error: String) ->
                     Unit = { _, error ->
                         context.snack(error)
                     }){
    val pd = ProgressDialog(context)
    pd.show()

    run1(this, ok, fail) { pd.dismiss() }
}

fun <T> Call<T>.run2(srRefresh: SwipeRefreshLayout,
                     ok: (body:T) -> Unit,
                     fail: (code: Int, error: String) -> Unit){
    run1(this, ok, fail) { srRefresh.isRefreshing = false }
}

fun <T> Call<T>.run2(ok: (body:T) -> Unit,
                     fail: (code: Int, error: String) -> Unit = {_,_ ->}){
    run1(this, ok, fail, {})
}



private fun <T> run1(call: Call<T>,
                     ok: (body:T) -> Unit,
                     fail: (code: Int, error: String) -> Unit,
                     stop: () -> Unit){
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            val body = response!!.body()
            if(response.isSuccessful) {
                if (body != null) ok(body)
                else {
                    fail(0,"Error")
                    norm("body is null")
                }
            } else {
                val bodyString = response.errorBody()?.string()
                norm("error body: $bodyString\n" +
                        "error status: ${response.code()}")
                val code = response.headers()["Error-Code"]?.toIntOrNull() ?: response.code()
                fail(code, Api.ERROR_CODE[code] ?: "Something went wrong")
                }
            stop()
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) {
            norm("<123>",t)
            fail(200, "Check your internet connection")
            stop()
        }

    })
}

fun File?.toMultiPartImage(field: String): MultipartBody.Part? {
    if (this == null) return null
    val body = RequestBody.create(MediaType.parse("image/*"), this)
    return MultipartBody.Part.createFormData(field, name, body)
}

private val formData = MediaType.parse("multipart/form-data")
fun String?.toMultiPart(): RequestBody {
    return RequestBody.create(formData, this?: "")
}
