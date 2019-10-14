package com.rc.magnesium.resource


import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.junit.rules.ExternalResource

class KafkaResourceRule extends ExternalResource {

    String bootstrapServers
    String topic

    Consumer<byte[], byte[]> consumer

    KafkaResourceRule(String bootstrapServers, String topic) {
        this.bootstrapServers = bootstrapServers
        this.topic = topic
    }

    @Override
    protected void before() throws Throwable {
        Map<String, Object> consumerProps = new HashMap<>()
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group")
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class)
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class)
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        
        this.consumer = new KafkaConsumer<>(consumerProps)
        consumer.subscribe(Collections.singletonList(topic))
    }

    @Override
    void after() {
        consumer.close()
    }
}
