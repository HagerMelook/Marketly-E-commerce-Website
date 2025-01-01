package com.market.backend.controllers;

import com.market.backend.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    private FeedbackService feedbackService;
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        feedbackService = Mockito.mock(FeedbackService.class);
        feedbackController = new FeedbackController(feedbackService);
    }

    @Test
    void addFeedback_shouldReturnSuccessResponse_whenFeedbackIsProcessedSuccessfully() {
        String feedbackBody = "This is a test feedback";
        doNothing().when(feedbackService).InsertFeedback(feedbackBody);

        ResponseEntity<String> response = feedbackController.addFeedback(feedbackBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Feedback received successfully!", response.getBody());
        verify(feedbackService, times(1)).InsertFeedback(feedbackBody);
    }

    @Test
    void addFeedback_shouldReturnErrorResponse_whenFeedbackProcessingFails() {
        String feedbackBody = "This is a test feedback";
        doThrow(new RuntimeException("Database error")).when(feedbackService).InsertFeedback(feedbackBody);

        ResponseEntity<String> response = feedbackController.addFeedback(feedbackBody);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your feedback.", response.getBody());
        verify(feedbackService, times(1)).InsertFeedback(feedbackBody);
    }
}

