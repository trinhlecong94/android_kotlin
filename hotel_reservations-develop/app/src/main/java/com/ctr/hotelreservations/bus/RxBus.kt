package com.ctr.hotelreservations.bus

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
object RxBus {
    private val publisher = PublishSubject.create<Any>()
    private val behavior = BehaviorSubject.create<Any>()

    /**
     * Emit item to listen
     */
    fun publish(event: Any) {
        publisher.onNext(event)
    }


    fun createBehaviorEvent(event: Any) {
        behavior.onNext(event)
    }

    /**
     * Listen should return an Observable and not the publisher
     * Using ofType we filter only events that match that class type
     */
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

    fun <T> listenBehavior(eventType: Class<T>): Observable<T> = behavior.ofType(eventType)
}
