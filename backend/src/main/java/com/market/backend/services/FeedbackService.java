package com.market.backend.services;

import com.market.backend.models.Feedback;
import com.market.backend.repositories.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void InsertFeedback(String body){
        Feedback feedback = Feedback.builder()
                        .body(body)
                        .build();
        feedbackRepository.save(feedback);
    }
}
