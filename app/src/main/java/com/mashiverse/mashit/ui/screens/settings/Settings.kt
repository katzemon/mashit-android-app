package com.mashiverse.mashit.ui.screens.settings

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.R
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.helpers.sys.checkNotificationsPermission
import com.mashiverse.mashit.utils.helpers.sys.getNotificationsPermission

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Settings() {
    val activity = LocalActivity.current
    val config = LocalConfiguration.current

    val ctx = LocalContext.current
    val viewModel = hiltViewModel<SettingsViewModel>()

    val notifications = viewModel.notificationsFlow.collectAsState(false)
    val isNotifications by remember(notifications.value) { mutableStateOf(notifications.value) }

    val specialDrops = viewModel.specialDropsFlow.collectAsState(false)
    val isSpecialDrops by remember(specialDrops.value) { mutableStateOf(specialDrops.value) }

    val updateNotifications = { enabled: Boolean ->
        if (enabled) {
            if (!checkNotificationsPermission(ctx)) {
                activity?.let {
                    getNotificationsPermission(ctx, activity)
                }
            }
        }
        viewModel.updateNotifications(enabled)
    }

    val resId = listOf(
        R.drawable.mashup,
        R.drawable.mashup2,
        R.drawable.mashup3,
        R.drawable.mashup4
    ).random()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .wrapContentSize(unbounded = true)
                .height(config.screenHeightDp.dp)
                .clipToBounds()
                .blur(
                    13.dp
                )
                .drawWithContent {
                    drawContent()
                    drawRect(Color.Black.copy(alpha = 0.7f))
                },
            contentDescription = null,
            painter = painterResource(resId),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Padding),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    modifier = Modifier.width(256.dp),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = ContentAccentColor,

                        ),
                    border = BorderStroke(width = 1.dp, Color.White),
                    shape = ContentContainerShape,
                    onClick = { viewModel.onDisconnect() },
                    contentPadding = PaddingValues(horizontal = Padding)
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Disconnect wallet",
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp
                    )
                }
            }

            CheckRow(
                title = "Opt in to new releases",
                checked = isNotifications
            ) { checked ->
                updateNotifications.invoke(checked)
            }

            Spacer(modifier = Modifier.height(SmallPadding))

            CheckRow(
                title = "Disable special drops",
                checked = isSpecialDrops
            ) { checked ->
                viewModel.updateSpecialDrops(checked)
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp),
            text = "mashup by u/Snek",
            color = ContentAccentColor.copy(alpha = 0.3F),
            fontSize = 10.sp
        )
    }
}