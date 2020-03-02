package com.squareup.reactworkflow

import android.os.Bundle
import com.squareup.workflow.ui.Compatible

/**
 * TODO write documentation
 */
data class ReactNativeRendering(
    val moduleName: String,
    val props: Bundle,
    val eventHandlers: Map<String, (params: Any?) -> Unit> = emptyMap()
) : Compatible {
    override val compatibilityKey: String get() = moduleName
}
