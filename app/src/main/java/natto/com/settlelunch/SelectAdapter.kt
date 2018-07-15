package natto.com.settlelunch

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.widget.LinearLayout


class SelectAdapter(context: Context) : BaseAdapter() {
    private var mContext: Context? = null
    private var mLayoutInflater: LayoutInflater? = null
    private var mItems:ArrayList<SelectModel>?=null

    private class ViewHolder {
        var gTextView: TextView? = null
        var gImageView: ImageView? = null
        var mBackLayout:LinearLayout?=null
    }

    init {
        mContext = context
        mLayoutInflater = LayoutInflater.from(context)
        mItems= ArrayList()
    }

    override fun getItem(pos: Int): SelectModel {
        return mItems!![pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {
        return mItems!!.size
    }

    fun add(item:SelectModel){
        mItems?.add(item)
    }

    fun setItem(item:SelectModel,index:Int){
        mItems?.set(index,item)
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        var v=convertView
        if (v == null) {
            v = mLayoutInflater?.inflate(R.layout.item_select, null)
            holder = ViewHolder()
            holder.gTextView = v?.findViewById(R.id.select_text) as TextView
            holder.gImageView = v?.findViewById(R.id.select_check) as ImageView
            holder.mBackLayout=v?.findViewById(R.id.select_bg) as LinearLayout

            v?.tag = holder
        } else {
            holder = v.tag as ViewHolder
        }

        holder.gTextView?.text= mItems!![pos].foodName
        if(mItems!![pos].isChecked) {
            holder.gTextView?.setTextColor(Color.parseColor("#ffffff"))
            holder.mBackLayout?.setBackgroundResource(R.drawable.shape_fix)
            holder.gImageView?.setImageResource(R.drawable.baseline_check_circle_outline_white_48dp)
        }else{
            holder.gTextView?.setTextColor(mContext?.resources?.getColor(R.color.colorMain)!!)
            holder.mBackLayout?.setBackgroundResource(R.drawable.shape_outline)
            holder.gImageView?.setImageDrawable(null)
        }

        return v!!
    }
}