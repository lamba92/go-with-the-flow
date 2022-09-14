package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration.Companion.seconds

suspend fun <T> FlowCollector<T>.emitAll(vararg items: T) = items.forEach { emit(it) }

suspend fun main() {
    val stringFlow = flow {
        emit("a")
        delay(1.seconds)
        emit("b")
        delay(1.seconds)
        emit("c")
        delay(1.seconds)
    }
    val intFlow = flow {
        emitAll(1, 2, 3)
        delay(1.1.seconds)
        emitAll(4, 5, 6)
        delay(1.seconds)
        emitAll(7, 8, 9)
    }

    stringFlow.modifiedBy(intFlow) { string, int -> string + int }
        .collect { println(it) }

}



/**
 *
 */
internal inline fun <reified T, reified R> Flow<T>.modifiedBy(
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