package com.xebia

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, DefaultConsumer, Envelope}
import com.xebia.util.DelayedMessageProcessingUtil

object ConsumerFactory extends DelayedMessageProcessingUtil {
  def getDefaultPushBackConsumer(channel: Channel) = {
    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) = {
        val bodyAsString = new String(body)
        println(s"$consumerTag processed message $bodyAsString at ${properties.getHeaders}")
        val delay = properties.getHeaders.get("x-delay").asInstanceOf[Int]
        if(delay * 2 < 7000)
        channel.basicPublish(envelope.getExchange, "", getDelayValuePropsBuilder(delay * 2).build(), bodyAsString.getBytes)
      }
    }
    consumer
  }

  def getDefaultDelayedConsumer(channel: Channel) = {
    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]) =
        println(s"$consumerTag processed message ${new String(body)} at ${properties.getHeaders}")
    }
    consumer
  }

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
