package com.example.payment_test

data class SupplementaryReceiptData(
    val developerSuppliedText: String?,
    val developerSuppliedImageUrlPath: String?,
    val barcodeOrQrcodeImageText: String?,
    val textImageType: TextImageFormat?
)

enum class TextImageFormat {
    QR_CODE,
    AZTEC_BARCODE
}