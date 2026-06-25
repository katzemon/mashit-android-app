package com.mashiverse.mashit.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.nav.graphs.mainGraph
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.modals.SignInModal
import com.mashiverse.mashit.ui.nav.bottom.BottomNavBar
import com.mashiverse.mashit.ui.screens.auth.Auth
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.delegates.createAppKitDelegate
import com.mashiverse.mashit.utils.helpers.sys.checkNotificationsPermission
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.reown.appkit.client.AppKit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Main(navController: NavHostController) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()

    val viewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val ctx = LocalContext.current

    var isSignIn by remember { mutableStateOf(false) }
    var isLurking by remember { mutableStateOf(false) }

    AppKit.setDelegate(
        createAppKitDelegate(
            onSessionApproved = { walletPrefs ->
                viewModel.updateWallet(walletPrefs)
                isSignIn = false
                isLurking = false
            },
            onSessionRejected = { isSignIn = false }
        )
    )

    val signInState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStack by navController.currentBackStackEntryAsState()
    val hasSearch by remember {
        derivedStateOf {
            backStack?.destination?.hasRoute<MainRoutes.Artists>() != true
        }
    }

    val searchQuery = remember { mutableStateOf("") }
    val onSearchQueryChange = remember {
        { input: String ->
            searchQuery.value = input
        }
    }

    var isSearch by remember {
        mutableStateOf(false)
    }

    val clearSearchQuery = {
        searchQuery.value = ""
        focusManager.clearFocus(true)
        isSearch = false
    }

    val dialogContent by remember {
        viewModel.dialogContent
    }


    val onIsSearchChange = remember {
        { isSearch = !isSearch }
    }

    val walletPreferences by viewModel.walletPreferences.collectAsState(WalletPreferences(null))

    val firstLaunch = viewModel.firstLaunchPreferences.collectAsState(false)

    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(firstLaunch.value) {
        if (firstLaunch.value) {
            viewModel.setDialogContent(
                DialogContent(
                    title = "Important",
                    text = "Please grant notifications permission. We care about your silence!"
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(50.milliseconds)
        isReady = true
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> }

    val onFirstLaunchDialogClose = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            val isGranted = checkNotificationsPermission(ctx)
        }
        viewModel.setFirstLaunchCompleted()
        viewModel.clearDialog()
    }

    val isKeyboardVisible = WindowInsets.isImeVisible

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
            isSearch = false
        }
    }

    LaunchedEffect(backStack?.destination?.route) {
        clearSearchQuery.invoke()
    }

    val mashup by viewModel.mashupFlow.collectAsState(null)

    val onSignIn = {
        isSignIn = true
    }

    if (isReady) {
        if (walletPreferences.wallet != null || isLurking) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Background,
                bottomBar = {
                    BottomNavBar(
                        navController = navController,
                        mashup = mashup ?: MashupDetails(),
                        searchQuery = searchQuery.value,
                        onSearchQueryChange = onSearchQueryChange,
                        isSearch = isSearch,
                        onIsSearchChange = onIsSearchChange,
                        hasSearch = hasSearch,
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                    )
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = MainRoutes.Shop(listingId = null),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = SmallPadding)
                ) {
                    mainGraph(
                        onSignIn = onSignIn,
                        searchQuery = searchQuery,
                        clearSearchQuery = clearSearchQuery
                    )
                }

                if (drawerState.isOpen) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                scope.launch { drawerState.close() }
                            }
                    )
                }
            }
        } else {
            Auth(
                onLurking = {
                    isLurking = true
                },
                onAuth = {
                    isSignIn = true
                }
            )
        }
    }

    if (isSignIn) {
        SignInModal(
            sheetState = signInState,
            onDismissRequest = {
                isSignIn = false
            }
        )
    }

    if ((isLurking || walletPreferences.wallet != null) && firstLaunch.value && dialogContent != null) {
        Dialog(dialogContent!!) {
            onFirstLaunchDialogClose.invoke()
        }
    }
}