package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow
import kotlinx.coroutines.rx3.rxObservable


suspend fun main(): Unit = coroutineScope {
    val observable = rxObservable { repeat(100) { send(it) } }

    val (evenFlow, oddFlow) = observable.asFlow()
        .partition { it % 2 == 0 }

    evenFlow.map {  }
    launch { evenFlow.collect { println("I am even: $it") } }
    launch { oddFlow.collect { println("I am odd: $it") } }
}

/**
 * Returns two flows, the first one has the values that passes the filter, the second one the values
 * which doesn't pass the filter.
 *
 * ![partition image](https://rxjs.dev/assets/images/marble-diagrams/partition.png)
 */
inline fun <T> Flow<T>.partition(crossinline filter: suspend (T) -> Boolean): Pair<Flow<T>, Flow<T>> {
    TODO()
}