import com.rabbitmq.client.Channel
import com.xebia.{ConsumerFactory, RabbitMQHelper}
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification

class ActiveMQSpecs extends Specification {

  sequential

  trait QueueScope extends Scope {
    val singleConsumerQueue = "single.consumer"
    val multiConsumerQueue = "multi.consumer"
    val ackConsumerQueue = "ack.consumer"
    val message = "Hello World"

    def publishMessage(channel: Channel, message: String, queue: String) = {
      channel.basicPublish("", queue, null, message.getBytes)
    }
  }

  "Queue" should {
    "continuously deliver messages one after the other to the same consumer" in new QueueScope {
      val channel = new RabbitMQHelper("192.168.99.100", 8081).getChannelWithQueue(singleConsumerQueue)
      val consumer = ConsumerFactory.getDefaultConsumer(channel)
      1 to 3 foreach (i => publishMessage(channel, s"$message $i", singleConsumerQueue))
      channel.basicConsume(singleConsumerQueue, true, consumer)
      channel.messageCount(singleConsumerQueue) shouldEqual 0
    }
  }
  "divide the work equally among multiple consumers" in new QueueScope {
    val channel = new RabbitMQHelper("192.168.99.100", 8081).getChannelWithQueue(multiConsumerQueue)
    val consumer1 = ConsumerFactory.getDefaultConsumer(channel)
    val consumer2 = ConsumerFactory.getDefaultConsumer(channel)
    channel.basicConsume(multiConsumerQueue, true, "consumer 1", false, false, null, consumer1)
    channel.basicConsume(multiConsumerQueue, true, "consumer 2", false, false, null, consumer2)
    1 to 5 foreach (i => publishMessage(channel, s"$message $i", multiConsumerQueue))
    channel.consumerCount(multiConsumerQueue) shouldEqual 2
    channel.messageCount(multiConsumerQueue) shouldEqual 0
  }
  "remove the message from the queue only after receiving ack from consumer" in new QueueScope {
    val channel = new RabbitMQHelper("192.168.99.100", 8081).getChannelWithQueue(ackConsumerQueue)
    1 to 5 foreach (i => publishMessage(channel, s"$message $i", ackConsumerQueue))
    val consumer = ConsumerFactory.getDefaultConsumerWithAck(channel)
    channel.basicConsume(ackConsumerQueue, false, "ack consumer", false, false, null, consumer)
    channel.consumerCount(ackConsumerQueue) shouldEqual 1
    channel.messageCount(ackConsumerQueue) shouldEqual 0
  }
}
