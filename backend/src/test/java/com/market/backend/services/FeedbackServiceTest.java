package com.market.backend.services;

import com.market.backend.models.Feedback;
import com.market.backend.repositories.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class FeedbackServiceTest {

    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedbackService = new FeedbackService(feedbackRepository);
    }

    @Test
    void testInsertFeedback() {
        String feedbackBody = "This is a test feedback";
        feedbackService.InsertFeedback(feedbackBody);
        ArgumentCaptor<Feedback> feedbackArgumentCaptor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(feedbackArgumentCaptor.capture());
        Feedback capturedFeedback = feedbackArgumentCaptor.getValue();
        assertEquals(feedbackBody, capturedFeedback.getBody(), "The feedback body should match");
    }
}