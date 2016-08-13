package com.xebia

import com.rabbitmq.client.ConnectionFactory

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
}
