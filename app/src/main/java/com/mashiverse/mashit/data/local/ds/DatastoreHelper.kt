package com.mashiverse.mashit.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mashiverse.mashit.utils.FIRST_LAUNCH_KEY
import com.mashiverse.mashit.utils.MASHIT_PREFERENCES
import com.mashiverse.mashit.utils.MORE_TRAITS_KEY
import com.mashiverse.mashit.utils.NOTIFICATIONS_KEY
import com.mashiverse.mashit.utils.SPECIAL_DROPS_KEY
import com.mashiverse.mashit.utils.WALLET_KEY
import com.mashiverse.mashit.utils.WALLET_TYPE_KEY

val Context.datastore by preferencesDataStore(
    name = MASHIT_PREFERENCES
)

object PreferencesKeys {
    val WALLET = stringPreferencesKey(WALLET_KEY)
    val WALLET_TYPE = stringPreferencesKey(WALLET_TYPE_KEY)
    val FIRST_LAUNCH = booleanPreferencesKey(FIRST_LAUNCH_KEY)
    val NOTIFICATIONS = booleanPreferencesKey(NOTIFICATIONS_KEY)
    val SPECIAL_DROPS = booleanPreferencesKey(SPECIAL_DROPS_KEY)
    val MORE_TRAITS = booleanPreferencesKey(MORE_TRAITS_KEY)
}