@file:Suppress("DuplicatedCode")

package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll

suspend fun main(): Unit = coroutineScope {
    val range = 0 until 100

    val jobs = range.asFlow()
        .groupBy { it % 10 }
        .map { (key, flow) ->
            flow.onEach { number -> println("Key $key -> $number") }
                .launchIn(this)
        }
        .toList()
    jobs.joinAll()
}

data class GroupByItem<K, V>(val key: K, val flow: Flow<V>)

/**
 * Returns a flow of flows, where each new flow emits the values grouped by the result of the [filter].
 *
 * ![groupBy image](https://rxjs.dev/assets/images/marble-diagrams/groupBy.png)
 */
fun <V, K> Flow<V>.groupBy(
    keySelector: suspend (V) -> K
): Flow<GroupByItem<K, V>> {
    TODO()
}