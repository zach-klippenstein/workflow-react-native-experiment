package com.squareup.reactworkflow

import android.app.Application
import com.facebook.react.ReactInstanceManager
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage

/**
 * TODO write documentation
 */
class MainApplication : Application() {

    val instanceManager by lazy {
        ReactInstanceManager.builder()
            .setApplication(this)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackage(MainReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
            .build()
    }
}
