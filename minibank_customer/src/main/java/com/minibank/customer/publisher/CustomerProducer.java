package com.minibank.customer.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.minibank.customer.domain.entity.Customer;
import com.minibank.customer.exception.SystemException;


@Component
public class CustomerProducer {
    @Autowired
    private KafkaTemplate<String, Customer> customerKafkaTemplate;

    @Value(value = "${creating.customer.topic.name}")
    private String creatingCustomerTopicName;

    public void sendCreatingCustomerMessage(Customer customer) {
        ListenableFuture<SendResult<String, Customer>> future = customerKafkaTemplate.send(creatingCustomerTopicName, customer);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Customer>>() {
    		private final Logger LOGGER = LoggerFactory.getLogger(CustomerProducer.class);
    		
            @Override
            public void onSuccess(SendResult<String, Customer> result) {
                Customer g = result.getProducerRecord().value();
                LOGGER.info("Sent message=[" + g.getCstmId() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable t) {
                // needed to do compensation transaction.
            	LOGGER.error( "Unable to send message=[" + customer.getCstmId() + "] due to : " + t.getMessage());
                throw new SystemException("Kafka 데이터 전송 에러", t);
            }
        });
    }
}
