package com.example.payment_test

data class TransactionRequest(
    val amount: Int?,
    val offlineReference: String?,
    val supplementaryReceiptData: SupplementaryReceiptData?,
    val metadata: Map<String, Any>?
)