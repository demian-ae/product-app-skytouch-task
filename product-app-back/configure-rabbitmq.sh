#!/bin/bash

# Path to the RabbitMQ config file
RABBITMQ_CONF_PATH="/etc/rabbitmq/rabbitmq.conf"

echo "Configuring RabbitMQ with environment variables"

echo "listeners.tcp.default = ${RABBITMQ_PORT}" >> ${RABBITMQ_CONF_PATH}

echo "management.listener.port = ${RABBITMQ_MANAGEMENT_PORT}" >> ${RABBITMQ_CONF_PATH}

cat ${RABBITMQ_CONF_PATH}

exec rabbitmq-server
