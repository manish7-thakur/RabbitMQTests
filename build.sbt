name := "RabbitMQTests"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++=  Seq(
  "org.specs2" % "specs2_2.11" % "3.7",
  "com.rabbitmq" % "amqp-client" % "3.6.5"
)
