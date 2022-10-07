package com.github.lamba92.gowiththeflow

import kotlinx.coroutines.*
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.rx3.collect
import kotlinx.coroutines.rx3.rxObservable
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

fun rxRange(count: Int) = rxObservable {
    repeat(count) {
        send(it)
        delay(0.1.seconds)
    }
}

suspend fun main() {

    val kotlinxObservable2 = rxObservable {
        delay(0.5.seconds)
        send(Unit)
    }

    val evenObservable = rxRange(10).filter { it % 2 == 0 }
    val oddObservable = rxRange(10).filter { it % 2 != 0 }

    evenObservable.collect { println("EVEN: $it") }
    oddObservable.collect { println("ODD: $it") }

    println("io.reactivex.rxjava3.core.Observable.take(long, java.util.concurrent.TimeUnit)")
    rxRange(10).take(800, TimeUnit.MILLISECONDS)
        .collect { println(it) }

    println("io.reactivex.rxjava3.core.Observable.takeUntil(io.reactivex.rxjava3.core.ObservableSource<U>)")
    rxRange(10).takeUntil(kotlinxObservable2)
        .collect { println(it) }

    println("io.reactivex.rxjava3.core.Observable.groupBy(io.reactivex.rxjava3.functions.Function<? super T,? extends K>)")
    coroutineScope {
        val jobs: List<Job> = rxRange(25).groupBy { it % 10 }
            .map { groupedObservable ->
                val key = groupedObservable.key
                launch {
                    groupedObservable.collect { integer ->
                        println("KEY $key -> $integer")
                    }
                }
            }
            .toList()
            .await()

        jobs.joinAll()
    }

}