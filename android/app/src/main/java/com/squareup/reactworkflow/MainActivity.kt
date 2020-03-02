package com.squareup.reactworkflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.soloader.SoLoader
import com.squareup.workflow.diagnostic.SimpleLoggingDiagnosticListener
import com.squareup.workflow.ui.WorkflowRunner
import com.squareup.workflow.ui.setContentWorkflow

class MainActivity : AppCompatActivity() {

    //    private val rootView by lazy { ReactRootView(this) }
    private val app by lazy { (application as MainApplication) }
    private val instanceManager by lazy { app.instanceManager }
    private val containerHints by lazy { app.containerHints }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoLoader.init(this, false)
        println(instanceManager)
//        val initialProps = Bundle().also {
//            it.putString("name", "World")
//        }
//        rootView.startReactApplication(instanceManager, "MyReactNativeApp", initialProps)
//        setContentView(rootView)
        setContentWorkflow(containerHints) {
            WorkflowRunner.Config(
                MainWorkflow(),
                diagnosticListener = SimpleLoggingDiagnosticListener()
            )
        }
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
//        rootView.unmountReactApplication()
    }
}