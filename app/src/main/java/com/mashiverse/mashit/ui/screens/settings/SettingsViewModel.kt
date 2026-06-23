package com.mashiverse.mashit.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.mashiverse.mashit.data.repos.sys.DatastoreRepo
import com.reown.appkit.client.AppKit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastoreRepo: DatastoreRepo
) : ViewModel() {
    val notificationsFlow = datastoreRepo.notificationsFlow
    val specialDropsFlow = datastoreRepo.specialDropsFlow

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (enabled) {
                FirebaseMessaging.getInstance().subscribeToTopic("all_users")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all_users")
            }

            datastoreRepo.updateNotifications(enabled)
        }
    }

    fun updateSpecialDrops(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepo.updateSpecialDrops(enabled)
        }
    }

    fun onDisconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepo.removeWallet()
        }
        AppKit.disconnect(
            onSuccess = { },
            onError = { }
        )
    }
}