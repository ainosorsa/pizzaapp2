
package com.example.pizzaapp.data


import com.example.pizzaapp.R


object DataSource {
    //poistuu?
    val flavors = listOf(
        R.string.pepperon,
        R.string.cheese,
        R.string.onion,
        R.string.chicken,
        R.string.pineapple,
        R.string.beacon,
        R.string.ham
    )

    //lisää koolle data
    val sizes = listOf(
        R.string.medium,
        R.string.large
    )
    //dipit
    val dips = listOf(
        R.string.chili,
        R.string.curry,
        R.string.ranch,
        R.string.garlic
    )


    val quantityOptions = listOf(
        Pair(R.string.americano, 1),
        Pair(R.string.opera, 2),
        Pair(R.string.tropicana, 3),
        Pair(R.string.mexicana, 4)
    )
}
