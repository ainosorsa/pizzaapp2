package com.example.pizzaapp

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.pizzaapp.data.DataSource
import com.example.pizzaapp.data.OrderUiState
import com.example.pizzaapp.ui.OrderSummaryScreen
import com.example.pizzaapp.ui.OrderViewModel
import com.example.pizzaapp.ui.SelectOptionScreen
import com.example.pizzaapp.ui.StartOrderScreen
import com.example.pizzaapp.R


enum class PizzaScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    //Flavor(title = R.string.choose_flavor), //poistuu?

    //Size(title = R.string.choose_size ),
    SizeAndDip(title = R.string.choose_sizeanddip),
    Dip(title = R.string.choose_dip),

    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}


@Composable
fun PizzaAppBar(
    currentScreen: PizzaScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun PizzaApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = PizzaScreen.valueOf(
        backStackEntry?.destination?.route ?: PizzaScreen.Start.name
    )

    Scaffold(
        topBar = {
            PizzaAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = PizzaScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = PizzaScreen.Start.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setQuantity(it)
                        navController.navigate(PizzaScreen.SizeAndDip.name) //aikasemmin Flavor
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = PizzaScreen.SizeAndDip.name) {// aikasemmin Flavor
                // tähän samaan dip?
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(PizzaScreen.Pickup.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = DataSource.sizes.map { id -> context.resources.getString(id) }, //aikasemmin flavors
                    onSelectionChanged = { viewModel.setSize(it) }, //Aikasemmin setFlavor
                    //???
                    options2 = DataSource.dips.map { id -> context.resources.getString(id) },
                    onSelectionChanged2 = {viewModel.setDip(it)},//????
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = PizzaScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(PizzaScreen.Summary.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.pickupOptions,
                    options2 = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onSelectionChanged2  = { viewModel.setDate(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = PizzaScreen.Summary.name) {
                val context = LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

/**
 * Resets the [OrderUiState] and pops up to [PizzaScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(PizzaScreen.Start.name, inclusive = false)
}

/**
 * Creates an intent to share order details
 */
private fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_pizza_order)
        )
    )
}