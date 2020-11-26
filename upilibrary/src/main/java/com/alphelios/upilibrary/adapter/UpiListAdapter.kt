package com.alphelios.upilibrary.adapter

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alphelios.upilibrary.R
import com.bumptech.glide.Glide

class UpiListAdapter : RecyclerView.Adapter<UpiListAdapter.AppViewHolder>() {

    private lateinit var mContext: Context
    private var mData: MutableList<ResolveInfo> = mutableListOf()
    private var mOnUpiListItemListener: OnUpiListItemListener? = null


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        mContext = parent.context
        val layout = LayoutInflater.from(mContext).inflate(R.layout.wangsun_upi_payment_item_upi_list, parent, false)
        return AppViewHolder(layout)
    }

    override
    fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    fun setListener(listener: OnUpiListItemListener){
        mOnUpiListItemListener = listener
    }

    fun setData(data: MutableList<ResolveInfo>){
        mData = data
        notifyDataSetChanged()
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(info: ResolveInfo) {
            itemView.findViewById<TextView>(R.id.item_upi_list_txt_name).text = info.loadLabel(mContext.packageManager).toString()
            Glide.with(itemView)
                .load(info.loadIcon(mContext.packageManager))
                .into(itemView.findViewById(R.id.item_upi_list_img_icon))

            itemView.findViewById<FrameLayout>(R.id.item_upi_list_fl).setOnClickListener {
                mOnUpiListItemListener?.onItemClick(info)
            }
        }
    }

    interface OnUpiListItemListener {
        fun onItemClick(data: ResolveInfo)
    }
}
