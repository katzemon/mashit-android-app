package com.mashiverse.mashit.ui.screens.shop.regular

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.data.states.web3.Web3Intent
import com.mashiverse.mashit.ui.default.buttons.BuyButton
import com.mashiverse.mashit.ui.default.images.DefaultImage
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPadding

@Composable
fun ShopItem(
    nft: Nft,
    processShopIntent: (ShopIntent) -> Unit,
    clientRef: CoinbaseWalletSDK?,
    processImageIntent: (ImageIntent) -> Unit,
    processWeb3Intent: (Web3Intent) -> Unit,
) {
    val productInfo = nft.productInfo

    val soldQty = productInfo?.soldQuantity ?: 0
    val delisted = nft.productInfo?.delisted ?: false
    val isSoldOut = soldQty >= (productInfo?.quantity ?: -1)

    Column {
        DefaultImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f),
            onClick = { processShopIntent(ShopIntent.OnNftSelect(productInfo?.id ?: "")) },
            data = nft.compositeUrl,
            processImageIntent = processImageIntent,
        )

        Spacer(modifier = Modifier.height(ExtraSmallPadding))

        Text(
            text = nft.name,
            fontSize = 14.sp,
            color = ContentAccentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(ExtraSmallPadding))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nft.author,
                    fontSize = 12.sp,
                    color = ContentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(ExtraSmallPadding))

                Text(
                    text = "$soldQty of ${productInfo?.quantity ?: -1} sold",
                    fontSize = 12.sp,
                    color = ContentColor
                )
            }

            Spacer(Modifier.width(ExtraSmallPadding))

            BuyButton(
                text = when {
                    isSoldOut -> "Sold out"
                    delisted -> "Delisted"
                    else -> "${productInfo?.price?.toInt()} ${productInfo?.priceCurrency?.name}"
                },
                enabled = !isSoldOut && !delisted,
                onClick = {
                    if (nft.productInfo?.listingId != null) {
                        processWeb3Intent(
                            Web3Intent.OnMint(
                                client = clientRef,
                                listingId = nft.productInfo.listingId,
                                price = nft.productInfo.price,
                                isPolCurrency = nft.productInfo.priceCurrency == PriceCurrency.POL
                            )
                        )
                    }
                }
            )
        }
    }
}