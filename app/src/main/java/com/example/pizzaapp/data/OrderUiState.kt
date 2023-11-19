package com.example.pizzaapp.data

/**
 * Data class that represents the current UI state in terms of [quantity], [flavor],
 * [dateOptions], selected pickup [date] and [price]
 */
data class OrderUiState(

    val quantity: Int = 0,

    //poistuu?
    //val flavor: String = "",

    val size: String = "",
    val dip: String= "",

    /** Selected date for pickup (such as "Jan 1") */
    val date: String = "",
    /** Total price for the order */
    val price: String = "",
    /** Available pickup dates for the order*/
    val pickupOptions: List<String> = listOf()
)
