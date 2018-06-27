package kz.vozon.vozon.utility

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kz.vozon.vozon.R
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


/*
Log
 */
private const val tag_prefix = "xxx"
private const val tag_norm = "$tag_prefix<->norm"
private const val tag_hana = "$tag_prefix<->hana"

fun Any.norm(text: String?,tr : Throwable? = null){
    if (tr == null) Log.d(tag_norm,"${this::class.java.simpleName}: $text")
    else Log.d(tag_norm,"${this::class.java.simpleName}: $text",tr)
}



fun AppCompatActivity.addBackPress(){
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setDisplayShowHomeEnabled(true)
}

fun Activity.snack(text: String){
    val rootView = window.decorView.findViewById<View>(android.R.id.content)
    val snackBar = Snackbar.make(rootView,text, Snackbar.LENGTH_LONG)
    snackBar.view.setBackgroundColor(Color.BLACK)
    val id = android.support.design.R.id.snackbar_text
    val textView = snackBar.view.findViewById(id) as TextView
    textView.setTextColor(Color.LTGRAY)
    snackBar.show()
}

fun Activity.ask(call:()->Unit){
    val builder = AlertDialog.Builder(this)
    builder.setMessage("Вы уверены?").setPositiveButton(getString(R.string.yes)) { _, _ ->  call()}
            .setNegativeButton(getString(R.string.no)) { _, _ ->  }.show()
}

fun Activity.alert(message:String ,call:()->Unit){
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message).setPositiveButton(getString(R.string.yes)) { _, _ ->  call()}.show()
}

fun Activity.toast(text: String){
    Toast.makeText(this,text, Toast.LENGTH_LONG).show()
}

fun View.snack(text: String){
    Snackbar.make(this,text, Snackbar.LENGTH_LONG).show()
}



fun EditText.get(error: String = ""): String{
    val text = this.text.toString().trim()
    if (error.isNotBlank() && text.isBlank())
        throw error(error)
    return text
}

var TextView.textIf: String?
    get() = text.toString()
    set(value) {
        if (value.isNullOrBlank().not()){
            text = value
        }
    }

var EditText.content : String?
    get() = text.toString()
    set(value) {
        setText(value, TextView.BufferType.EDITABLE)
    }

fun ImageView.src(src: String?, res: Int){

    var picasso = Picasso.get().load(src)
    if (res == R.drawable.tmp_truck){
        picasso = picasso.resize(100,100)
    }
    picasso.placeholder(res).into(this)
}

fun ImageView.addFilter(res:Int = R.attr.appIconColor){
    val typedValue = TypedValue()
    context.theme.resolveAttribute(res, typedValue, true)
    val color = typedValue.data
    this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}


@Suppress("UNCHECKED_CAST")
fun <T: Serializable> Intent.get(type : Class<T>): T{
    return this.getSerializableExtra(type.simpleName) as T
}

fun Intent.put(data: Serializable){
    this.putExtra(data::class.java.simpleName,data)
}

fun Activity.compressImage(uri: Uri): File {
    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

    val file = File(cacheDir,  "${System.currentTimeMillis()}.jpg")
    val os = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
    os.flush()
    os.close()

    return file
}


fun clientOrCourier() : Class<*>? = when (Shared.currentUser.type){
    kz.vozon.vozon.models.User.CLIENT -> kz.vozon.vozon.ui.client.XMainActivity::class.java
    kz.vozon.vozon.models.User.COURIER -> kz.vozon.vozon.ui.courier.YMainActivity::class.java
    else -> {
        Shared.currentUser = kz.vozon.vozon.models.User()
        null
    }
}

fun String.dateFormat(): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    return SimpleDateFormat("d, MMMM", Locale.getDefault()).format(date)
}

fun String.dateFormat(time: String): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    val month = SimpleDateFormat("d, MMMM", Locale.getDefault()).format(date)
    return "${time.substring(0,time.length-3)} ● $month"
}



fun setDateListener(activity: Activity) = View.OnClickListener {view ->
    val calendar = Calendar.getInstance()
    DatePickerDialog(activity,
            DatePickerDialog.OnDateSetListener {
                _, year, month, dayOfMonth ->
                (view as EditText).content = "$year-${month+1}-$dayOfMonth"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
}

private fun Int.format() = when{
    this<10 -> "0$this"
    else -> "$this"
}

fun setTimeListener(activity: Activity) = View.OnClickListener {view ->
    TimePickerDialog(activity,
            TimePickerDialog.OnTimeSetListener {
                _, hourOfDay, minute ->
                (view as EditText).content = "${hourOfDay.format()}:${minute.format()}"
            }, 0, 0, true).show()
}

fun Activity.askPermission(permission: String,requestCode: Int): Boolean {
    var isNeed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    isNeed = isNeed &&
            ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
    if (isNeed) ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    return !isNeed
}



