package com.xebia.util

import scala.collection.JavaConversions._
import com.rabbitmq.client.AMQP.BasicProperties

trait DelayedMessageProcessingUtil {
  def getDelayValuePropsBuilder(delay: Int) = {
    val props = new BasicProperties.Builder()
    props.headers(Map("x-delay" -> new Integer(delay)))
  }

  def getDelayedHeaderProps = Map("x-delayed-type" -> "direct")
}
