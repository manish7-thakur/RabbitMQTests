# RabbitMQTests
The applications tests several capabilities that RabbitMQ provides. The tests were executed against a running rabbitmq server inside a docker
container. The specs are written using Specs2 with Scala as the base language

The tests for delayed message processing uses a plugin `rabbitmq_delayed_message_exchange` not available with docker default images. Use 
the Dockerfile in the project source to build the docker image with delayed message processing. The Docker image is built on top of the 
rabbitmq image with management plugin. The Dockerfile assumes the plugin is available in the same directory.

To build use:

`docker build -t rabbitmq:3-delayedexchange .`

To run use :

`docker run -d --hostname my-rabbit --name some-rabbit -p 8081:5672 -p 8080:15672 rabbitmq:3-delayedexchange`

The rabbitmq management application runs on `15672` mapped to `8080` port from host & rabbitmq server runs on `5672` mapped to `8081`.

