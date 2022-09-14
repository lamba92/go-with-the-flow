package com.github.lamba92.gowiththeflow

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observables.GroupedObservable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx3.asObservable
import kotlinx.coroutines.rx3.rxFlowable
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

    flow.takeUntil(2.seconds)
        .collect { print(it) }
}

/**
 * Returns a flow that terminates after the given [duration].
 */
fun <T> Flow<T>.takeUntil(duration: Duration): Flow<T> = TODO()