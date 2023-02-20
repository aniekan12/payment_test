package com.example.payment_test

import androidx.annotation.NonNull
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import io.flutter.embedding.android.FlutterFragmentActivity


class MainActivity: FlutterFragmentActivity() {

    private val CHANNEL = "flutterwave.irecharge/payments";
    val TRANSACTION_RESULT_CODE = 14
    val TRANSACTION = "com.paystack.pos.TRANSACT"
    val startActivityForResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), intentResultCallback())
    val gson = Gson()


    private fun intentResultCallback(): ActivityResultCallback<ActivityResult> {

        return ActivityResultCallback { result: ActivityResult ->
            val resultCode = result.resultCode
            val intent = result.data
            val paystackIntentResponse: PaystackIntentResponse
            val terminalResponse: TerminalResponse

            if (resultCode == TRANSACTION_RESULT_CODE) {
                paystackIntentResponse = gson.fromJson(
                    intent?.getStringExtra(TRANSACTION),
                    PaystackIntentResponse::class.java
                )
                terminalResponse = paystackIntentResponse.intentResponse
                val transactionResponse: TransactionResponse = gson.fromJson(
                    terminalResponse.data,
                    TransactionResponse::class.java
                )

                Toast.makeText(
                    applicationContext,
                    "Transaction ref: " + transactionResponse.reference + "\nAmount: " +  transactionResponse.amount + "\nStatus: " + transactionResponse.status + "\nPaid At: " + transactionResponse.paidAt +  "\nTerminal: " + transactionResponse.terminal,
                    Toast.LENGTH_LONG
                ).show()
            }
            else {
                // handle invalid result code
            }
        }
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method.equals("processTransactions")) {
                val amount = call.argument<Int>("amount");
                val flutterwave = processTransactions(amount);

                if (flutterwave != null) {
                    result.success(flutterwave);
                } else {
                    result.error("UNAVAILABLE", "Flutterwave not available.", null);
                }
            } else {
                result.notImplemented();
            }
        }
    }


    private fun processTransactions(amount: Int?) {

        val transactionRequest = TransactionRequest(
            amount = amount,
            offlineReference = null,
            supplementaryReceiptData = null,
            metadata = mapOf(
                "custom_fields" to listOf(
                    CustomField(
                        display_name = "Extra Detail",
                        variable_name = "extra_detail",
                        value = "1234"
                    )
                )
            )
        )

        val transactionIntent = Intent(Intent.ACTION_VIEW).apply {
            setPackage("com.paystack.pos")
            putExtra("com.paystack.pos.TRANSACT", gson.toJson(transactionRequest))
        }

        startActivityForResult.launch(transactionIntent)
    }

}
