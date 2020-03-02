package com.squareup.reactworkflow

import android.os.Bundle
import com.squareup.reactworkflow.MainWorkflow.State
import com.squareup.workflow.*
import kotlinx.coroutines.delay

/**
 * TODO write documentation
 */
class MainWorkflow : StatefulWorkflow<Unit, State, Nothing, ReactNativeRendering>() {

    data class State(val name: String)

    private val worker = Worker.create {
        while (true) {
            emit("Other World")
            delay(1000)
            emit("World")
            delay(1000)
        }
    }

    override fun initialState(props: Unit, snapshot: Snapshot?): State =
        State("World")

    override fun render(
        props: Unit,
        state: State,
        context: RenderContext<State, Nothing>
    ): ReactNativeRendering {
        context.runningWorker(worker, handler = ::setName)

        return ReactNativeRendering(
            moduleName = "MyReactNativeApp",
            props = Bundle().apply { putString("name", state.name) },
            eventHandlers = mapOf(
                "onClick" to { params: Any? -> context.actionSink.send(onClick(params)) }
            )
        )
    }

    override fun snapshotState(state: State): Snapshot = Snapshot.EMPTY
}

private fun setName(name: String) = action<State, Nothing> {
    nextState = State(name)
}

private fun onClick(params: Any?) = action<State, Nothing> {
    nextState = State("${nextState.name} (clicked: $params)")
}
