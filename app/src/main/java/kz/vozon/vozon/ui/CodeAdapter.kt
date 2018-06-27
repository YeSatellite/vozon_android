package kz.vozon.vozon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_code_selected.view.*
import kotlinx.android.synthetic.main.item_info_tmp.view.*
import kz.vozon.vozon.R

class CodeAdapter(val context: Context, val list: List<kz.vozon.vozon.models.Country>): BaseAdapter(){
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = View.inflate(context, R.layout.item_code_selected,null)
        v.v_code.text = list[position].phoneCode!!
        return v
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = View.inflate(context, R.layout.item_info_tmp2,null)
        v.v_transport.text = list[position].name!!

        return v
    }

    override fun getItem(position: Int) = list[position]
    override fun getItemId(position: Int) = 0L
    override fun getCount() = list.size
}
