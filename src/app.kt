import java.math.BigDecimal
import java.time.LocalDateTime

class SendOrderEmail: Subscriber {
    override fun handle(payload: HashMap<String, Any>) {
        val customerName = payload["customerName"]
        val totalAmount = payload["totalAmount"]

        println("Email sent to $customerName with order totalAmount equals $totalAmount")
    }
}

class GenerateInvoice: Subscriber {
    override fun handle(payload: HashMap<String, Any>) {
        val customerName = payload["customerName"]
        val totalAmount = payload["totalAmount"]

        println("Invoice generated for $customerName with order totalAmount equals $totalAmount")
    }
}

class OrderClosed(private val order: Order): Event {
    override val name = "orderClosed"

    override fun buildPayload(): HashMap<String, Any> {
        return hashMapOf(
            "customerName" to order.customerName,
            "totalAmount" to order.totalAmount
        )
    }
}

data class Order(
    val customerName: String,
    val totalAmount: BigDecimal
) {
    private var closedAt: LocalDateTime? = null

    val events: MutableList<Event> = mutableListOf()

    fun close() {
        closedAt = LocalDateTime.now()
        this.events.add(OrderClosed(this))
    }
}

object OrdersRepository{

    fun create(order: Order) {
        println("Order created")

        publishEvents(order)
    }

    fun update(order: Order) {
        println("Order updated")

        publishEvents(order)
    }

    private fun publishEvents(order: Order) {
        for (event in order.events) {
            EventBus.publish(event)
        }
    }
}

fun main() {
    EventBus.addSubscriber("orderClosed", SendOrderEmail())
    EventBus.addSubscriber("orderClosed", GenerateInvoice())

    val order = Order("Nicolas Zein", BigDecimal("100.30"))
    OrdersRepository.create(order)

    order.close()

    OrdersRepository.update(order)
}