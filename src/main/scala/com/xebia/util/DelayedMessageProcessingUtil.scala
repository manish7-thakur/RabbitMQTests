package com.xebia.util

import java.util

import com.rabbitmq.client.AMQP.BasicProperties

trait DelayedMessageProcessingUtil {
  def getDelayValuePropsBuilder(delay: Int) = {
    val props = new BasicProperties.Builder()
    val headers = new util.HashMap[String, Object]()
    headers.put("x-delay", new Integer(delay))
    props.headers(headers)
  }

  def getDelayedHeaderProps = {
    val argus = new util.HashMap[String, Object]()
    argus.put("x-delayed-type", "direct")
    argus
  }
}
