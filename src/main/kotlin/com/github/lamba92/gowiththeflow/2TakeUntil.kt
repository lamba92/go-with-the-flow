package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


suspend fun main() {
    val flow = flow {
        emit("Hello")
        delay(1.seconds)
        emit(" world!")
        delay(2.seconds)
        emit("nope!")
    }

    val stoppingFlow = flow {
        delay(1.5.seconds)
        emit(Unit)
    }

    flow.takeUntil(1.5.seconds)
        .collect { print(it) }

    println()

    flow.takeUntil(stoppingFlow)
        .collect { print(it) }
}


/**
 * Returns a flow that terminates once [flow] emits a value.
 *
 * ![takeUntil image](https://rxjs.dev/assets/images/marble-diagrams/takeUntil.png)
 */
fun <T> Flow<T>.takeUntil(flow: Flow<*>): Flow<T> = TODO()

/**
 * Returns a flow that terminates after the given [duration].
 */
fun <T> Flow<T>.takeUntil(duration: Duration): Flow<T> = TODO()
