package com.sicredi.desafio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SqsServiceTest {

    private SqsService sqsService;

    @Mock
    private SqsClient sqsClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String queueUrl = "queueUrl";
        sqsService = new SqsService(sqsClient, queueUrl);
    }

    @Test
    void sendMessage_shouldInvokeSqsClientSendMessage() {
        String messageBody = "Test Message";

        sqsService.sendMessage(messageBody);

        SendMessageRequest expectedRequest = SendMessageRequest.builder()
                .queueUrl("queueUrl")
                .messageBody(messageBody)
                .build();
        verify(sqsClient, times(1)).sendMessage(expectedRequest);
    }

    @Test
    void receiveMessages_shouldInvokeSqsClientReceiveMessage() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl("queueUrl")
                .build();
        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .build();
        when(sqsClient.receiveMessage(request)).thenReturn(response);

        ReceiveMessageResponse result = sqsService.receiveMessages();

        verify(sqsClient, times(1)).receiveMessage(request);
        assertEquals(result, response);
    }

    @Test
    void deleteMessage_shouldInvokeSqsClientDeleteMessage() {
        String receiptHandle = "receiptHandle";

        sqsService.deleteMessage(receiptHandle);

        DeleteMessageRequest expectedRequest = DeleteMessageRequest.builder()
                .queueUrl("queueUrl")
                .receiptHandle(receiptHandle)
                .build();
        verify(sqsClient, times(1)).deleteMessage(expectedRequest);
    }
}
