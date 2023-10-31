package com.github.pokatomnik.kriper.ext

interface Subscription {
    fun unsubscribe()
}

interface Subscriber<T : Any?> {
    fun subscribe(fn: (value: T) -> Unit): Subscription
}

interface Publisher<T : Any?> {
    fun publish(value: T)
}

class PubSub<T : Any?>: Publisher<T>, Subscriber<T> {
    private val subscribers = mutableSetOf<(value: T) -> Unit>()

    override fun subscribe(fn: (value: T) -> Unit): Subscription {
        subscribers.add(fn)
        return object : Subscription {
            override fun unsubscribe() {
                subscribers.remove(fn)
            }
        }
    }

    override fun publish(value: T) {
        subscribers.forEach { it(value) }
    }

}