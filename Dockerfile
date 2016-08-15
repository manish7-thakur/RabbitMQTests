FROM rabbitmq:3-management
ADD rabbitmq_delayed_message_exchange-0.0.1.ez /plugins
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange
