package com.xebia

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, DefaultConsumer, Envelope}

object ConsumerFactory {
  def getDefaultConsumerWithAck(channel: Channel) = {
    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) = {
        println(s"$consumerTag processed message ${new String(body)}")
        channel.basicAck(envelope.getDeliveryTag, false)
      }
    }
    consumer
  }

  def getDefaultConsumer(channel: Channel) = {
    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) =
        println(s"$consumerTag processed message ${new String(body)}")
    }
    consumer
  }
}
