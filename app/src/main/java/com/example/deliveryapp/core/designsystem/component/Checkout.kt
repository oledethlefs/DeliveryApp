package com.example.deliveryapp.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deliveryapp.R
import com.example.deliveryapp.core.data.model.fritzKolaBox033
import com.example.deliveryapp.core.data.model.fritzKolaBox05
import com.example.deliveryapp.core.data.model.fritzKolaSingle033
import com.example.deliveryapp.core.data.model.fritzKolaSingle05
import com.example.deliveryapp.core.designsystem.theme.DeliveryAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.format
import java.util.Locale

/**
 * A composable that displays the summary of the checkout process, including the breakdown
 * of product prices, deposits, and service fees, along with the final total price.
 * It also contains the interaction button to complete the purchase.
 *
 * @param totalPrice The subtotal for all products in the cart (in cents).
 * @param totalDeposit The subtotal for all bottle or crate deposits (in cents).
 * @param serviceFee The fee charged for the delivery service (in cents).
 * @param onPurchase Callback triggered when the user initiates the purchase action.
 * @param purchaseCompleted Boolean state indicating whether the purchase transaction has been finished.
 * @param modifier The [Modifier] to be applied to the footer layout.
 */
@Composable
fun CheckoutFooter(
    totalPrice: Int,
    totalDeposit: Int,
    serviceFee: Int,
    onPurchase: () -> Unit,
    purchaseCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.all_prices_with_taxes_note),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.total_price_of_products),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatPrice(totalPrice),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.total_price_of_deposits),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatPrice(totalDeposit),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.delivery_fee),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatPrice(serviceFee),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.total_price),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatPrice(totalPrice+totalDeposit+serviceFee),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))


        SlideToPurchaseButton(
            onPurchase = onPurchase,
            isCompleted = purchaseCompleted
        )
    }
}


/**
 * Formats a price given in total cents into a currency string.
 * For example, 128 becomes "1,28€".
 *
 * @param cents The price in cents.
 * @return The formatted currency string.
 */
fun formatPrice(cents: Int): String {
    val euros = cents / 100
    val remainingCents = cents % 100
    return String.format(Locale.GERMANY, "%d,%02d€", euros, remainingCents)
}

@Preview (showBackground = true)
@Composable
fun CheckoutFooterPreview() {
    DeliveryAppTheme {
        val totalPrice = fritzKolaSingle033.price + fritzKolaBox033.price * 2 + fritzKolaBox05.price + fritzKolaSingle05.price
        val totalDeposit = fritzKolaSingle033.deposit + fritzKolaBox033.deposit * 2 + fritzKolaBox05.deposit + fritzKolaSingle05.deposit

        var isPaymentCompleted by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        CheckoutFooter(
            totalPrice = totalPrice,
            totalDeposit = totalDeposit,
            serviceFee = 199,
            purchaseCompleted = isPaymentCompleted,
            onPurchase = {
                coroutineScope.launch {
                    delay(2000)
                    isPaymentCompleted = true
                }
            },
        )

    }
}