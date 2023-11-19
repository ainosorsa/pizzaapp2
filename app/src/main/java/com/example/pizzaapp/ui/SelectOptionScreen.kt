package com.example.pizzaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import com.example.pizzaapp.R


import com.example.pizzaapp.ui.components.FormattedPriceLabel




@Composable
fun SelectOptionScreen(
    subtotal: String,
    options: List<String>,
    options2: List<String>,


    onSelectionChanged: (String) -> Unit = {},
    onSelectionChanged2: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},

    modifier: Modifier = Modifier
) {
    var selectedValue by rememberSaveable { mutableStateOf("") }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            options.forEach { item ->
                Row(
                    modifier = Modifier.selectable(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)


                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    RadioButton(
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item
                            onSelectionChanged(item)
                        }
                    )
                    Text(item)
                }
            }
        }
        //button2
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                options2.forEach { item ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged2(item)


                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        RadioButton(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged2(item)
                            }
                        )
                        Text(item)
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .weight(1f, false),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedButton(modifier = Modifier.weight(1f), onClick = onCancelButtonClicked) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    // the button is enabled when the user makes a selection
                    enabled = selectedValue.isNotEmpty(),
                    onClick = onNextButtonClicked
                ) {
                    Text(stringResource(R.string.next))
                }

            }
        }
    }
}

@Preview
@Composable
fun SelectOptionPreview() {
    SelectOptionScreen(
        subtotal = "299.99",
        options = listOf("Option 1", "Option 2", "Option 3", "Option 4", ),
        //??
        options2 = listOf("Option 1", "Option 2", "Option 3", "Option 4",),

        modifier = Modifier.fillMaxHeight(),

        )
}
