package com.market.backend.controllers;

import com.market.backend.models.Feedback;
import com.market.backend.models.VendorRequest;
import com.market.backend.services.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private AdminService adminService;

    @Test
    void getFeedback_ShouldReturnFeedbackList() throws Exception {
        List<Feedback> feedbacks = Arrays.asList(new Feedback(1L, "Great!"), new Feedback(2L, "Needs improvement"));
        when(adminService.getFeedbacks()).thenReturn(feedbacks);
        mockMvc.perform(get("/feedback"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].comment").value("Great!"));

        verify(adminService, times(1)).getFeedbacks();
    }

    @Test
    void deleteFeedback_ShouldReturnSuccessMessage() throws Exception {
        long feedbackId = 1L;
        doNothing().when(adminService).deleteFeedback(feedbackId);

        mockMvc.perform(delete("/feedback/{feedbackId}", feedbackId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback deleted successfully"));

        verify(adminService, times(1)).deleteFeedback(feedbackId);
    }

    @Test
    void getVendorRequests_ShouldReturnVendorRequestList() throws Exception {
        List<VendorRequest> requests = Arrays.asList(new VendorRequest(1L, "Request 1"), new VendorRequest(2L, "Request 2"));
        when(adminService.getVendorRequests()).thenReturn(requests);

        mockMvc.perform(get("/request"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Request 1"));

        verify(adminService, times(1)).getVendorRequests();
    }

    @Test
    void acceptVendorRequest_ShouldReturnSuccessMessage() throws Exception {
        long requestId = 1L;
        doNothing().when(adminService).addVendor(requestId);

        mockMvc.perform(post("/request/accept/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vendor has been added successfully"));

        verify(adminService, times(1)).addVendor(requestId);
    }

    @Test
    void deleteVendorRequest_ShouldReturnSuccessMessage() throws Exception {
        long requestId = 1L;
        doNothing().when(adminService).declineVendorRequest(requestId);

        mockMvc.perform(delete("/request/delete/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vendor deleted successfully"));

        verify(adminService, times(1)).declineVendorRequest(requestId);
    }

    @Test
    void deleteFeedback_ShouldReturnBadRequestOnException() throws Exception {
        long feedbackId = 1L;
        doThrow(new IllegalArgumentException("Invalid feedback ID")).when(adminService).deleteFeedback(feedbackId);
        mockMvc.perform(delete("/feedback/{feedbackId}", feedbackId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid feedback ID"));

        verify(adminService, times(1)).deleteFeedback(feedbackId);
    }





}
