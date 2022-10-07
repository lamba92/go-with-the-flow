package com.github.lamba92.gowiththeflow.solutions

import com.github.lamba92.gowiththeflow.GroupByItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration


/**
 * Returns two flows, the first one has the values that passes the filter, the second one the values
 * which doesn't pass the filter.
 *
 * ![partition image](https://rxjs.dev/assets/images/marble-diagrams/partition.png)
 */
private inline fun <T> Flow<T>.partition(crossinline filter: suspend (T) -> Boolean): Pair<Flow<T>, Flow<T>> {
    val flowA = flow {
        collect {
            if (filter(it)) emit(it)
        }
    }

    val flowB = flow {
        collect {
            if (!filter(it)) emit(it)
        }
    }
    return flowA to flowB
}

/**
 * Returns a flow that terminates after the given [duration].
 */
private fun <T> Flow<T>.takeUntil(duration: Duration): Flow<T> = channelFlow {
    val job = launch { collect { send(it) } }
    delay(duration)
    job.cancel()
}

/**
 * Returns a flow that terminates once [flow] emits a value.
 *
 * ![takeUntil image](https://rxjs.dev/assets/images/marble-diagrams/takeUntil.png)
 */
private fun <T> Flow<T>.takeUntil(flow: Flow<*>): Flow<T> = channelFlow {
    val job = launch { collect { send(it) } }
    flow.first()
    job.cancel()
}

/**
 * Returns a flow of flows, where each new flow emits the values grouped by the result of the [filter].
 *
 * ![groupBy image]()https://rxjs.dev/assets/images/marble-diagrams/groupBy.png)
 */
private inline fun <V, K> Flow<V>.groupBy(
    crossinline filter: suspend (V) -> K
): Flow<GroupByItem<K, V>> {
    val flowMap = mutableMapOf<K, Channel<V>>()
    val flow = flow {
        try {
            collect { value ->
                val key = filter(value)
                val isKeyPresent = key !in flowMap
                val channel = flowMap.getOrPut(key) { Channel(Channel.UNLIMITED) }
                if (isKeyPresent) emit(GroupByItem(key, channel.consumeAsFlow()))
                channel.send(value)
            }
        } finally {
            flowMap.values.forEach { it.close() }
        }
    }

    return flow
}
