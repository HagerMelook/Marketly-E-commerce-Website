package com.market.backend.controllers;

import com.market.backend.configurations.JWTFilter;
import com.market.backend.models.Account;
import com.market.backend.models.Feedback;
import com.market.backend.models.VendorRequest;
import com.market.backend.services.AdminService;
import com.market.backend.services.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({JWTService.class, JWTFilter.class})
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private AdminService adminService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .id(1L)
                .username("testuser")
                .isActive(true)
                .type("client")
                .authType("basic")
                .build();
    }

    @Test
    void testGetUserInfoByUserName_Success() throws Exception {
        when(adminService.getAccountInfoByUserName("testuser")).thenReturn(testAccount);

        mockMvc.perform(get("/admin/info/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(adminService, times(1)).getAccountInfoByUserName("testuser");
    }

    @Test
    void testGetUserInfoByUserName_NotFound() throws Exception {
        when(adminService.getAccountInfoByUserName("nonexistent")).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/admin/info/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        verify(adminService, times(1)).getAccountInfoByUserName("nonexistent");
    }

    @Test
    void testActivateUserAccount_Success() throws Exception {
        doNothing().when(adminService).changeAccountStatus(true, 1L);

        mockMvc.perform(put("/admin/activate/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account status changed successfully"));

        verify(adminService, times(1)).changeAccountStatus(true, 1L);
    }

    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testActivateUserAccount_NotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(adminService).changeAccountStatus(true, 999L);

        mockMvc.perform(put("/admin/activate/999"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).changeAccountStatus(true, 999L);
    }


    @Test
    void testPromoteAccount_Success() throws Exception {
        doNothing().when(adminService).promoteAccount(1L);

        mockMvc.perform(put("/admin/promote/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account promoted successfully"));

        verify(adminService, times(1)).promoteAccount(1L);
    }

    @Test
    void testPromoteAccount_NotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(adminService).promoteAccount(999L);

        mockMvc.perform(put("/admin/promote/999"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).promoteAccount(999L);
    }

    @Test
    void testPromoteAccount_Unauthorized() throws Exception {
        doThrow(new IllegalArgumentException("Unauthorized")).when(adminService).promoteAccount(1L);

        mockMvc.perform(put("/admin/promote/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));

        verify(adminService, times(1)).promoteAccount(1L);
    }

    @Test
    void testDemoteAccount_Success() throws Exception {
        doNothing().when(adminService).demoteAccount(1L);

        mockMvc.perform(put("/admin/demote/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account demoted successfully"));

        verify(adminService, times(1)).demoteAccount(1L);
    }

    @Test
    void testDemoteAccount_NotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(adminService).demoteAccount(999L);

        mockMvc.perform(put("/admin/demote/999"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).demoteAccount(999L);
    }

    @Test
    void testDemoteAccount_Unauthorized() throws Exception {
        doThrow(new IllegalArgumentException("Unauthorized")).when(adminService).demoteAccount(1L);

        mockMvc.perform(put("/admin/demote/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));

        verify(adminService, times(1)).demoteAccount(1L);
    }


    @Test
    void testDeleteAccount_Success() throws Exception {
        doNothing().when(adminService).deleteAccount(1L);

        mockMvc.perform(delete("/admin/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully"));

        verify(adminService, times(1)).deleteAccount(1L);
    }

    @Test
    void testDeleteAccount_BadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Invalid ID")).when(adminService).deleteAccount(999L);

        mockMvc.perform(delete("/admin/delete/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid ID"));

        verify(adminService, times(1)).deleteAccount(999L);
    }

    @Test
    void getFeedback_ShouldReturnFeedbackList() throws Exception {
        List<Feedback> feedbacks = Arrays.asList(new Feedback(1L, "Great!"), new Feedback(2L, "Needs improvement"));
        when(adminService.getFeedbacks()).thenReturn(feedbacks);
        mockMvc.perform(get("/admin/feedback"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].body").value("Great!"));

        verify(adminService, times(1)).getFeedbacks();
    }

    @Test
    void deleteFeedback_ShouldReturnSuccessMessage() throws Exception {
        long feedbackId = 1L;
        doNothing().when(adminService).deleteFeedback(feedbackId);

        mockMvc.perform(delete("/admin/feedback/{feedbackId}", feedbackId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feedback deleted successfully"));

        verify(adminService, times(1)).deleteFeedback(feedbackId);
    }

    @Test
    void getVendorRequests_ShouldReturnVendorRequestList() throws Exception {
        List<VendorRequest> requests = Arrays.asList(
                VendorRequest.builder().id(1L).username("Request 1").build(),
                VendorRequest.builder().id(2L).username("Request 2").build()
        );
        when(adminService.getVendorRequests()).thenReturn(requests);

        mockMvc.perform(get("/admin/request"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("Request 1"));

        verify(adminService, times(1)).getVendorRequests();
    }

    @Test
    void acceptVendorRequest_ShouldReturnSuccessMessage() throws Exception {
        long requestId = 1L;
        doNothing().when(adminService).addVendor(requestId);

        mockMvc.perform(post("/admin/request/accept/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vendor has been added successfully"));

        verify(adminService, times(1)).addVendor(requestId);
    }

    @Test
    void deleteVendorRequest_ShouldReturnSuccessMessage() throws Exception {
        long requestId = 1L;
        doNothing().when(adminService).declineVendorRequest(requestId);

        mockMvc.perform(delete("/admin/request/delete/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vendor deleted successfully"));

        verify(adminService, times(1)).declineVendorRequest(requestId);
    }

    @Test
    void deleteFeedback_ShouldReturnBadRequestOnException() throws Exception {
        long feedbackId = 1L;
        doThrow(new IllegalArgumentException("Invalid feedback ID")).when(adminService).deleteFeedback(feedbackId);
        mockMvc.perform(delete("/admin/feedback/{feedbackId}", feedbackId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid feedback ID"));

        verify(adminService, times(1)).deleteFeedback(feedbackId);
    }
}