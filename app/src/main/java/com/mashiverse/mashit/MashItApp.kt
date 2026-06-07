package com.mashiverse.mashit

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.mashiverse.mashit.utils.COINBASE_ID
import com.mashiverse.mashit.utils.METAMASK_ID
import com.mashiverse.mashit.utils.REOWN_ID
import com.mashiverse.mashit.utils.TRUST_ID
import com.reown.android.Core
import com.reown.android.CoreClient
import com.reown.android.relay.ConnectionType
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class MashItApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var appMetaData: Core.Model.AppMetaData
    @Inject
    lateinit var polygonChain: Modal.Model.Chain

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()

        if (Timber.treeCount == 0) Timber.plant(Timber.DebugTree())

        CoreClient.initialize(
            projectId = REOWN_ID,
            connectionType = ConnectionType.AUTOMATIC,
            application = this,
            metaData = appMetaData,
            onError = {}
        )

        AppKit.initialize(
            init = Modal.Params.Init(
                core = CoreClient,
                coinbaseEnabled = true,
                includeWalletIds = listOf(
                    METAMASK_ID,
                    COINBASE_ID,
                    TRUST_ID
                )
            ),
            onSuccess = { AppKit.setChains(listOf(polygonChain)) },
            onError = {}
        )
    }
}