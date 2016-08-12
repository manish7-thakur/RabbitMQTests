import com.rabbitmq.client.Channel
import com.xebia.{ConsumerFactory, RabbitMQHelper}
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class ActiveMQSpecs extends Specification {


  trait QueueScope extends Scope {
    val helloQueue = "HELLO"

    def publishMessage(channel: Channel, message: String) = {
      channel.basicPublish("", helloQueue, null, message.getBytes)
    }
  }

  "One worker" should {
    "continuously pick messages one after the other" in new QueueScope {
      val channel = new RabbitMQHelper("192.168.99.100", 8081).getChannelWithQueue(helloQueue)
      val message = "Hello World"
      publishMessage(channel, message)
      publishMessage(channel, message)
      publishMessage(channel, message)
      val consumer = ConsumerFactory.getDefaultConsumer(channel)
      channel.basicConsume(helloQueue, true, consumer)
      channel.messageCount(helloQueue) shouldEqual 0
    }
  }
}
