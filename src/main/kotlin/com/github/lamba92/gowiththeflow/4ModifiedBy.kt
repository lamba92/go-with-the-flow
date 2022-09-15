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
 * Returns a flow that emits all the original followed by a [fold] of
 * the last value from the receiver as initial accumulator with the values emitted
 * from the [modifierFlow].
 *
 * ![modifiedBy image](https://github.com/lamba92/go-with-the-flow/blob/master/images/modifiedBy_operator.png?raw=true)
 */
internal inline fun <reified T, reified R> Flow<T>.modifiedBy(
    modifierFlow: Flow<R>,
    crossinline transform: suspend (T, R) -> T
): Flow<T> = TODO()