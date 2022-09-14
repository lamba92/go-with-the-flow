package com.github.lamba92.gowiththeflow.solutions

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration


/**
 * Returns two flows, the first one has the values that passes the filter, the second one the ones which don't.
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

private data class GroupByItem<K, V>(val key: K, val flow: Flow<V>)

/**
 * Returns a flow of flows, where each new flow emits the values grouped by the result of the [filter].
 */
private inline fun <V, K> Flow<V>.groupBy(
    closeAllOnError: Boolean = true,
    crossinline filter: suspend (V) -> K
): Flow<GroupByItem<K, V>> {
    val flowMap = mutableMapOf<K, Channel<V>>()
    val flow: Flow<GroupByItem<K, V>> = try {
        channelFlow {
            collect { value ->
                val key = filter(value)
                val isKeyPresent = key !in flowMap
                val channel = flowMap.getOrPut(key) { Channel(Channel.UNLIMITED) }
                if (isKeyPresent) send(GroupByItem(key, channel.consumeAsFlow()))
                channel.send(value)
            }
        }
    } catch (e: Throwable) {
        if (closeAllOnError) flowMap.values.forEach { it.close() }
        throw e
    }
    flowMap.values.forEach { it.close() }
    return flow
}


/**
 * Returns a flow that emits all the original followed by a [fold] of
 * the last value from the receiver as initial accumulator with the values emitted
 * from the [modifierFlow].
 */
private inline fun <reified T, reified R> Flow<T>.modifiedBy(
    modifierFlow: Flow<R>,
    crossinline transform: suspend (T, R) -> T
): Flow<T> = channelFlow {
    val queue = Channel<Any?>(capacity = 1)

    val mutex = Mutex(locked = true)
    this@modifiedBy.onEach {
        queue.send(it)
        if (mutex.isLocked) mutex.unlock()
    }.launchIn(this)
        .invokeOnCompletion { queue.close() }

    mutex.lock()
    modifierFlow.onEach { queue.send(it) }.launchIn(this)

    var currentState: T = queue.receive() as T
    send(currentState)

    for (e in queue) {
        currentState = when (e) {
            is T -> e
            is R -> transform(currentState, e)
            else -> continue
        }
        send(currentState)
    }
}