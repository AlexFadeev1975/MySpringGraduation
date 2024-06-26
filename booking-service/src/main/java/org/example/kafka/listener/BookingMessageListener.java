package org.example.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.data.model.kafka.BookingMessage;
import org.example.services.StatisticService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingMessageListener {

    private final StatisticService statisticService;

    @KafkaListener(topics = "${spring.kafka.kafkaBookingMessageTopic}",
            groupId = "${spring.kafka.kafkaMessageGroupId}",
            containerFactory = "regMessageKafkaListenerContainerFactory")
    public void listener(@Payload BookingMessage message,
                         @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received message: {}", message);
        log.info(" Key: {}, Partition: {}, Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

        statisticService.saveBookingMessage(message).subscribe();
    }

}
