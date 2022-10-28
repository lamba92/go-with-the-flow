package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


suspend fun main() {
    val flow = flow {
        repeat(100) { count -> emit(count) }
    }

    val (evenFlow, oddFlow) = flow
        .partition { number -> number % 2 == 0 }

    coroutineScope {
        launch { evenFlow.collect { println("I am even: $it") } }
        oddFlow.collect { println("I am odd: $it") }
    }
}

/**
 * Returns two flows, the first one has the values that passes the filter, the second one the values
 * which doesn't pass the filter.
 *
 * ![partition image](https://rxjs.dev/assets/images/marble-diagrams/partition.png)
 */
fun <T> Flow<T>.partition(
    filter: suspend (T) -> Boolean
): Pair<Flow<T>, Flow<T>> {
    TODO()
}