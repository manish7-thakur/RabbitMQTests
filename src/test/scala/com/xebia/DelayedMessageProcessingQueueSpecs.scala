package com.xebia

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.xebia.util.DelayedMessageProcessingUtil
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification


class DelayedMessageProcessingQueueSpecs extends Specification with DelayedMessageProcessingUtil {

  sequential

  trait QueueUtils {
    val singleConsumerDelayedQueue = "single.consumer.delayed"
    val delayedExchange = "delayed-exchange"
    val message = "Hello World"
  }


  trait DelayedMessageProcessingScope extends Scope with QueueUtils {
    val channel = new RabbitMQHelper("192.168.99.100", 8081).getChannelWithQueue(singleConsumerDelayedQueue)
    channel.exchangeDeclare(delayedExchange, "x-delayed-message", false, false, getDelayedHeaderProps)
    val props = getDelayValuePropsBuilder(2000)
    channel.queueBind(singleConsumerDelayedQueue, delayedExchange, "")

    def publishMessage(channel: Channel, exchange: String, props: BasicProperties.Builder, message: String): Unit = {
      channel.basicPublish(delayedExchange, "", props.build(), message.getBytes)
    }
  }

  "DelayedMessageProcessingQueue" >> {
    "Consumer" should {
      "process message only after the specified delay" in new DelayedMessageProcessingScope {
        1 to 5 foreach (i => publishMessage(channel, delayedExchange, props, s"$message $i"))
        val delayedConsumer = ConsumerFactory.getDefaultDelayedConsumer(channel)
        channel.basicConsume(singleConsumerDelayedQueue, true, "delayed-consumer", false, false, null, delayedConsumer)
        channel.consumerCount(singleConsumerDelayedQueue) shouldEqual 1
        Thread.sleep(20000)
        channel.messageCount(singleConsumerDelayedQueue) shouldEqual 0
      }
      "put the message back with updated x-delay header value" in new DelayedMessageProcessingScope {
        1 to 5 foreach (i => publishMessage(channel, delayedExchange, props, s"$message $i"))
        val pushbackDelayedConsumer = ConsumerFactory.getDefaultPushBackConsumer(channel)
        channel.basicConsume(singleConsumerDelayedQueue, true, "pushback-delayed-consumer", false, false, null, pushbackDelayedConsumer)
        channel.consumerCount(singleConsumerDelayedQueue) shouldEqual 2
        Thread.sleep(40000)
        channel.messageCount(singleConsumerDelayedQueue) shouldEqual 0
      }
      "not push back message with delay greater than 7000 milliseconds" in new DelayedMessageProcessingScope {
        1 to 5 foreach (i => publishMessage(channel, delayedExchange, props, s"$message $i"))
        val pushbackDelayedConsumer = ConsumerFactory.getDefaultPushBackConsumer(channel)
        channel.basicConsume(singleConsumerDelayedQueue, true, "pushback-delayed-consumer", false, false, null, pushbackDelayedConsumer)
        channel.consumerCount(singleConsumerDelayedQueue) shouldEqual 3
        Thread.sleep(40000)
        channel.messageCount(singleConsumerDelayedQueue) shouldEqual 0
      }
    }
  }
}
