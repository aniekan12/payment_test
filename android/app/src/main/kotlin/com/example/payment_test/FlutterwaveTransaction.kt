package com.example.payment_test

data class FlutterwaveTransaction(
    val amount: String,
    val shouldPrint: Boolean,
    val transactionType: List<String>
    )
