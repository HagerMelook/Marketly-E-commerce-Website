package com.market.backend.controllers;

import com.market.backend.services.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add-feedback")
    public ResponseEntity<String> addFeedback(@RequestBody String feedbackBody) {
        try {
            feedbackService.InsertFeedback(feedbackBody);
            return ResponseEntity.ok("Feedback received successfully!");
        } catch (Exception e) {
            System.err.println("Error while saving feedback: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your feedback.");
        }
    }
}
