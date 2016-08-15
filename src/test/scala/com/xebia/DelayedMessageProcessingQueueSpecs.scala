package com.xebia

import java.util
import com.rabbitmq.client.AMQP.BasicProperties
import org.specs2.mutable.Specification


class DelayedMessageProcessingQueueSpecs extends Specification {

  val singleConsumerDelayedQueue = "single.consumer.delayed"

  val argus = new util.HashMap[String, Object]()
  argus.put("x-delayed-type", "direct")


  "DelayedMessageProcessingQueue" should {
    "process message only after the specified delay" in {
      val channel = new RabbitMQHelper("192.168.99.100", 8081).getChannelWithQueue(singleConsumerDelayedQueue)
      val delayedExchange = "delayed-exchange"
      channel.exchangeDeclare(delayedExchange, "x-delayed-message", false, false, argus)
      val props = new BasicProperties.Builder()
      val headers = new util.HashMap[String, Object]()
      headers.put("x-delay", new Integer(5000))
      props.headers(headers)
      channel.basicPublish(delayedExchange, "", props.build(), "Hello World".getBytes)
      val delayedConsumer = ConsumerFactory.getDefaultDelayedConsumer(channel)
      channel.basicConsume(singleConsumerDelayedQueue, true, "delayed-consumer", false, false, null, delayedConsumer)
      channel.messageCount(singleConsumerDelayedQueue) shouldEqual 0
    }
  }
}
