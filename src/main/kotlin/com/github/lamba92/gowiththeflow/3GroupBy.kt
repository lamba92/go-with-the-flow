@file:Suppress("DuplicatedCode")

package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList

suspend fun main(): Unit = coroutineScope {
    val range = 0..100

    val jobs = range.asFlow()
        .groupBy { it % 10 }
        .map { (key, flow) ->
            flow.onEach { println("Key $key -> $it") }
                .launchIn(this)
        }
        .toList()
}

data class GroupByItem<K, V>(val key: K, val flow: Flow<V>)

/**
 * Returns a flow of flows, where each new flow emits the values grouped by the result of the [filter].
 */
inline fun <V, K> Flow<V>.groupBy(
    closeAllOnError: Boolean = true,
    crossinline filter: suspend (V) -> K
): Flow<GroupByItem<K, V>> = TODO()