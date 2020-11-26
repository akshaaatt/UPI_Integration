package com.alphelios.upilibrary.ui


import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphelios.upilibrary.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.alphelios.upilibrary.UpiPayment
import com.alphelios.upilibrary.adapter.UpiListAdapter
import java.util.*

class UpiBottomSheet : BottomSheetDialogFragment() {

    private var mAdapter = UpiListAdapter()
    private var mOnUpiTypeSelectedListener: OnUpiTypeSelectedListener? =null
    private var mUpiApps: ArrayList<String> = arrayListOf()

    private lateinit var mUri: Uri
    private lateinit var mView: View
    private lateinit var recyclerView: RecyclerView

    override fun getTheme(): Int {
        return R.style.wangsun_style_Dialog_BottomSheet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUri = Uri.parse(arguments!!.getString("uri"))
        mUpiApps = arguments!!.getStringArrayList(UpiPayment.ARG_UPI_APPS_LIST)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mView =  inflater.inflate(R.layout.wangsun_upi_payment_upi_bottom_sheet, container, false)
        recyclerView = mView.findViewById(R.id.upi_bottom_sheet_recycler_view)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getAllUpiApps()

        mView.findViewById<ImageView>(R.id.upi_bottom_sheet_img_cancel_btn).setOnClickListener {
            mOnUpiTypeSelectedListener?.onUpiAppClosed()
            this.dismiss()
        }

        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getAllUpiApps() {
        val paymentIntent = Intent(Intent.ACTION_VIEW)
        paymentIntent.data = mUri
        val appList = context!!.packageManager.queryIntentActivities(paymentIntent, 0)


        if(mUpiApps.isNotEmpty()){
            val finalList: MutableList<ResolveInfo> = mutableListOf()
            for(i in appList){
                if(mUpiApps.contains(i.loadLabel(context!!.packageManager).toString().toLowerCase()))
                    finalList.add(i)
            }
            mAdapter.setData(finalList)
        }
        else{
            mAdapter.setData(appList)
        }

    }

    private fun initAdapter() {
        mAdapter.setListener(object : UpiListAdapter.OnUpiListItemListener{
            override fun onItemClick(data: ResolveInfo) {
                mOnUpiTypeSelectedListener?.onUpiAppSelected(data)
                this@UpiBottomSheet.dismiss()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
    }


    fun setListener(pListener: OnUpiTypeSelectedListener?) {
        mOnUpiTypeSelectedListener = pListener
    }


    interface OnUpiTypeSelectedListener {
        fun onUpiAppSelected(data: ResolveInfo)
        fun onUpiAppClosed()
    }
}