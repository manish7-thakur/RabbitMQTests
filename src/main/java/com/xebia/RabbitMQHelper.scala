package com.xebia

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, ConnectionFactory, DefaultConsumer, Envelope}

class RabbitMQHelper(host: String = "localhost", port: Int = 5672) {
  def getChannelWithQueue(queue: String) = {
    val factory = new ConnectionFactory()
    factory.setHost(host)
    factory.setPort(port)
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.queueDeclare(queue, false, false, false, null)
    channel
  }

  def consumeMessage(channel: Channel) = {
    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]): Unit =
        println(body.toString)
    }
    consumer
  }
}
