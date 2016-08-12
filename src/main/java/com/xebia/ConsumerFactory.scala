package com.xebia

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, DefaultConsumer, Envelope}

object ConsumerFactory {
  def getDefaultConsumer(channel: Channel) = {
    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]): Unit =
        println(body)
    }
    consumer
  }
}
