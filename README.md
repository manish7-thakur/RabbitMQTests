# RabbitMQTests
The applications tests several capabilities that RabbitMQ provides. The tests were executed against a running rabbitmq server inside a docker
container. The specs are written using Specs2 with Scala as the base language

The tests for delayed message processing uses a plugin `rabbitmq_delayed_message_exchange` not available with docker default images. Use 
the Dockerfile in the project source to build the docker image with delayed message processing. The Docker image is built on top of the 
rabbitmq image with management plugin.

To build use:

`docker build -t rabbitmq:3-delayedexchange .`

To run use :

`docker run -d --hostname my-rabbit --name some-rabbit -p 8081:5672 -p 8080:15672 rabbitmq:3-delayedexchange`

