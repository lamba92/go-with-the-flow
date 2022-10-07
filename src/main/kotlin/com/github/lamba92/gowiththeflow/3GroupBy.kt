@file:Suppress("DuplicatedCode")

package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val range = 0 until 100

    range.asFlow()
        .groupBy { it % 10 }
        .collect { (key, flow) ->
            launch {
                flow.collect { number ->
                    println("Key $key -> $number")
                }
            }
        }
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