package com.hifi.hifi_shopping_sales.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(priceStr: String): String {
    val price = priceStr.toIntOrNull() ?: return "Invalid Input"
    val formatter = NumberFormat.getNumberInstance(Locale("ko", "KR"))
    val formattedAmount = formatter.format(price)
    return "$formattedAmount 원"
}