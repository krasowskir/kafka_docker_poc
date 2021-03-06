package servicetests

import com.example.consumerApp.DemoApplication
import com.example.consumerApp.service.MyMessageReceiver
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@ContextConfiguration(initializers = [Initializer.class])
@Testcontainers
@SpringBootTest(classes = DemoApplication.class)
class MetricsSpec extends Specification {

    @Autowired
    MyMessageReceiver myMessageReceiver

    @Shared
    @ClassRule
    public static KafkaContainer kafka  = new KafkaContainer(DockerImageName.parse('confluentinc/cp-kafka:5.3.0'))

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    'spring.kafka.bootstrap-servers=' + kafka.getBootstrapServers())
                    .applyTo(configurableApplicationContext.getEnvironment())
        }
    }

    def setupSpec() {
        kafka.start()
    }

    def 'test order consumer is able to consume messages'() {

        given: 'a kafka template'
        def configs = new HashMap(KafkaTestUtils.producerProps(kafka.getBootstrapServers()))
        def factory = new DefaultKafkaProducerFactory<String, String>(configs, new StringSerializer(), new StringSerializer())
        def template = new KafkaTemplate<String, String>(factory, true)

        ProducerRecord<String, String> record = new ProducerRecord<>("testTopic", 0, null, '1234567890', "test-123")

        when: 'sending a message to kafka'
        template.send(record).get()

        then: 'the message is consumed successfully and forwarded to apps REST endpoint'
        new PollingConditions().within(5,() -> {
            myMessageReceiver.getCount() == 1
        })

    }
}