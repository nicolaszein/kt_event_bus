interface Subscriber {
    abstract fun handle(payload: HashMap<String, Any>)
}

interface Event {
    abstract val name: String

    abstract fun buildPayload(): HashMap<String, Any>
}

object EventBus{
    private var subscribers: HashMap<String, MutableList<Subscriber>> = HashMap()

    fun publish(event: Event) {
        for (subscriber in this.subscribers[event.name]!!) {
            subscriber.handle(event.buildPayload())
        }
    }

    fun addSubscriber(eventName: String, subscriber: Subscriber) {
        if(!this.subscribers.containsKey(eventName)){
            this.subscribers[eventName] = mutableListOf(subscriber)
        } else {
            this.subscribers[eventName]?.add(subscriber)
        }
    }
}