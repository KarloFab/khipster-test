package com.kfabija.test.web.rest

import com.kfabija.test.config.KafkaProperties
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JhipsterKafkaResourceIT {

    private lateinit var restMockMvc: MockMvc

    private var started = false

    private lateinit var kafkaContainer: KafkaContainer

    @BeforeAll
    fun startServer() {
        if (!started) {
            startTestcontainer()
            started = true
        }
    }

    private fun startTestcontainer() {
        // TODO: withNetwork will need to be removed soon
        // See discussion at https://github.com/jhipster/generator-jhipster/issues/11544#issuecomment-609065206
        kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.5")).withNetwork(null)
        kafkaContainer.start()
    }

    @BeforeEach
    fun setup() {
        val kafkaProperties = KafkaProperties()
        val producerProps = getProducerProps()
        kafkaProperties.setProducer(producerProps)

        val consumerGroups = getConsumerProps("default-group")
        consumerGroups["client.id"] = "default-client"
        kafkaProperties.setConsumer(consumerGroups)

        val kafkaResource = JhipsterKafkaResource(kafkaProperties)

        restMockMvc = MockMvcBuilders.standaloneSetup(kafkaResource).build()
    }

    @Test
    @Throws(Exception::class)
    fun producesMessages() {
        restMockMvc.perform(post("/api/jhipster-kafka/publish/topic-produce?message=value-produce"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        val consumerProps = getConsumerProps("group-produce")
        val consumer = KafkaConsumer<String, String>(consumerProps)
        consumer.subscribe(listOf("topic-produce"))
        val records = consumer.poll(Duration.ofSeconds(1))

        assertThat(records.count()).isEqualTo(1)
        val record = records.iterator().next()
        assertThat(record.value()).isEqualTo("value-produce")
    }

    @Test
    @Throws(Exception::class)
    fun consumesMessages() {
        val producerProps = getProducerProps()
        val producer = KafkaProducer<String, String>(producerProps)

        producer.send(ProducerRecord<String, String>("topic-consume", "value-consume"))

        val mvcResult = restMockMvc.perform(get("/api/jhipster-kafka/consume?topic=topic-consume"))
            .andExpect(status().isOk)
            .andExpect(request().asyncStarted())
            .andReturn()

        for (i in 0..100) {
            Thread.sleep(100)
            val content = mvcResult.getResponse().getContentAsString()
            if (content.contains("data:value-consume")) {
                return
            }
        }
        fail<String>("Expected content data:value-consume not received")
    }

    private fun getProducerProps(): MutableMap<String, Any> {
        val producerProps: MutableMap<String, Any> = HashMap()
        producerProps["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        producerProps["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        producerProps["bootstrap.servers"] = kafkaContainer.bootstrapServers
        return producerProps
    }

    private fun getConsumerProps(group: String): MutableMap<String, Any> {
        val consumerProps: MutableMap<String, Any> = HashMap()
        consumerProps["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        consumerProps["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        consumerProps["bootstrap.servers"] = kafkaContainer.bootstrapServers
        consumerProps["auto.offset.reset"] = "earliest"
        consumerProps["group.id"] = group
        return consumerProps
    }
}
