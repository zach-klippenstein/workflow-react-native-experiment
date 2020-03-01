package com.squareup.reactworkflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.ReactRootView
import com.facebook.soloader.SoLoader

class MainActivity : AppCompatActivity() {

    private val rootView by lazy { ReactRootView(this) }
    private val instanceManager by lazy { (application as MainApplication).instanceManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false)
        println(instanceManager)
        rootView.startReactApplication(instanceManager, "MyReactNativeApp", null)
        setContentView(rootView)
    }

    override fun onResume() {
        super.onResume()
        instanceManager.onHostResume(this, ::onBackPressed)
    }

    override fun onPause() {
        super.onPause()
        instanceManager.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        instanceManager.onHostDestroy(this)
        rootView.unmountReactApplication()
    }
}