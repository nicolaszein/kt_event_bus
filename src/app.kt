import java.math.BigDecimal

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

        println("Voice generated for $customerName with order totalAmount equals $totalAmount")
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
)

fun main() {
    val eventBus = EventBus()

    eventBus.addSubscriber("orderClosed", SendOrderEmail())
    eventBus.addSubscriber("orderClosed", GenerateInvoice())

    val order = Order("Nicolas Zein", BigDecimal("100.30"))

    eventBus.publish(OrderClosed(order))
}