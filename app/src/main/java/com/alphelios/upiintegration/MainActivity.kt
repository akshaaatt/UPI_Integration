package com.alphelios.upiintegration

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alphelios.upiintegration.databinding.ActivityMainBinding
import com.alphelios.upilibrary.UpiPayment
import com.alphelios.upilibrary.model.PaymentDetail
import com.alphelios.upilibrary.model.TransactionDetails

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val transactionId = "TID" + System.currentTimeMillis()
        binding.fieldTransactionRefId.setText(transactionId)

        // Setup click listener for Pay button
        binding.buttonPay.setOnClickListener { pay() }
    }

    private fun pay() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        val payeeVpa = binding.fieldVpa.text.toString()
        val payeeName = binding.fieldName.text.toString()
        val transactionRefId = binding.fieldTransactionRefId.text.toString()
        val payeeMerchantCode = binding.fieldPayeeMerchantCode.text.toString()
        val description = binding.fieldDescription.text.toString()
        val amount = binding.fieldAmount.text.toString()

        startUpiPayment(
            payeeVpa,
            payeeName,
            payeeMerchantCode,
            transactionRefId,
            description,
            amount
        )
    }

    private fun startUpiPayment(
        payeeVpa: String,
        payeeName: String,
        payeeMerchantCode: String,
        transactionRefId: String,
        description: String,
        amount: String
    ) {
        val payment = PaymentDetail(
            vpa = payeeVpa,
            name = payeeName,
            payeeMerchantCode = payeeMerchantCode,
            //txnId = "",
            txnRefId = transactionRefId,
            description = description,
            amount = amount
        )


        UpiPayment(this)
            .setPaymentDetail(payment)
//            .setUpiApps(UpiPayment.UPI_APPS)
            .setCallBackListener(object : UpiPayment.OnUpiPaymentListener {
                override fun onSubmitted(data: TransactionDetails) {
                    Toast.makeText(this@MainActivity, "$data", Toast.LENGTH_LONG).show()
                }

                override fun onSuccess(data: TransactionDetails) {
                    Toast.makeText(this@MainActivity, "$data", Toast.LENGTH_LONG).show()
                }

                override fun onError(message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            }).pay()
    }
}