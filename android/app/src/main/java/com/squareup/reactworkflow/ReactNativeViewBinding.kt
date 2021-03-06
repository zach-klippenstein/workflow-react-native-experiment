package com.squareup.reactworkflow

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.ViewBinding
import com.squareup.workflow.ui.bindShowRendering
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.reflect.KClass

/**
 * TODO write documentation
 */
class ReactNativeViewBinding(
    private val instanceManager: ReactInstanceManager,
    private val addHintsToReactProps: ((ContainerHints, props: Bundle) -> Unit)? = null
) : ViewBinding<ReactNativeRendering> {

    override val type: KClass<ReactNativeRendering> get() = ReactNativeRendering::class

    override fun buildView(
        initialRendering: ReactNativeRendering,
        initialContainerHints: ContainerHints,
        contextForNewView: Context,
        container: ViewGroup?
    ): View {
        val rootView = ReactRootView(contextForNewView)
        rootView.startReactApplication(
            instanceManager,
            initialRendering.moduleName,
            initialRendering.props.updateHints(initialContainerHints)
        )
        var eventHandlers = initialRendering.eventHandlers

        // Propagate workflow updates.
        rootView.bindShowRendering(initialRendering, initialContainerHints) { rendering, hints ->
            rootView.appProperties = rendering.props.updateHints(hints)
            eventHandlers = rendering.eventHandlers
        }

        // Wire up lifecycle events.
        val reactScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
        rootView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                // Noop
            }

            override fun onViewDetachedFromWindow(v: View) {
                reactScope.cancel()
                rootView.unmountReactApplication()
            }
        })

        // Wire up UI events.
        val eventsPackage =
            rootView.reactInstanceManager!!.packages.filterIsInstance<WorkflowEventsPackage>()
                .single()
        reactScope.launch {
            eventsPackage.events.collect { (name, params) ->
                eventHandlers[name]?.invoke(params) ?: run {
                    println("Warning: React Native sent workflow event but no handler found: $name($params)")
                }
            }
        }

        return rootView
    }

    private fun Bundle.updateHints(hints: ContainerHints): Bundle {
        addHintsToReactProps?.also { it(hints, this) }
        return this
    }
}
