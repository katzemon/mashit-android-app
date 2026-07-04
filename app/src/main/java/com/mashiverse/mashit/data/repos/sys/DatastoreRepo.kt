package com.mashiverse.mashit.data.repos.sys

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mashiverse.mashit.data.local.ds.PreferencesKeys
import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.data.models.sys.wallet.WalletType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreRepo @Inject constructor(
    private val datastore: DataStore<Preferences>
) {

    val walletFlow: Flow<WalletPreferences> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    Timber.tag("Testing").d(e)
                }
            }
            .map { preferences ->
                WalletPreferences(
                    wallet = preferences[PreferencesKeys.WALLET],
                    walletType = WalletType.valueOf(
                        preferences[PreferencesKeys.WALLET_TYPE] ?: "BASE"
                    )
                )
            }

    val firstLaunchFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    Timber.tag("Test").d(e)
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.FIRST_LAUNCH] ?: true
            }

    val notificationsFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    Timber.tag("Test").d(e)
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.NOTIFICATIONS] ?: false
            }

    val specialDropsFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    Timber.tag("Test").d(e)
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.SPECIAL_DROPS] ?: false
            }

    val moreTraitsFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    Timber.tag("Test").d(e)
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.MORE_TRAITS] ?: false
            }

    suspend fun updateWallet(walletPreferences: WalletPreferences) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.WALLET] = walletPreferences.wallet ?: ""
            preferences[PreferencesKeys.WALLET_TYPE] = walletPreferences.walletType.name
        }
    }

    suspend fun updateMoreTraits(enabled: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.MORE_TRAITS] = enabled
        }
    }

    suspend fun removeWallet() {
        datastore.edit { preferences ->
            preferences.remove(PreferencesKeys.WALLET)
        }
    }

    suspend fun setFirstLaunchCompleted() {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_LAUNCH] = false
        }
    }

    suspend fun updateNotifications(enabled: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }

    suspend fun updateSpecialDrops(enabled: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.SPECIAL_DROPS] = enabled
        }
    }
}