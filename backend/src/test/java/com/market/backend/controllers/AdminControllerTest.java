package com.market.backend.controllers;

import com.market.backend.models.Account;
import com.market.backend.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private AdminService adminService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setUsername("testuser");
        testAccount.setEmail("test@example.com");
        testAccount.setActive(true);
        testAccount.setType("client");
    }

    @Test
    void testGetUserInfoByUserName_Success() throws Exception {
        when(adminService.getAccountInfoByUserName("testuser")).thenReturn(testAccount);

        mockMvc.perform(get("/admin/info/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

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
    void testGetUserInfoByEmail_Success() throws Exception {
        when(adminService.getAccountInfoByEmail("test@example.com")).thenReturn(testAccount);

        mockMvc.perform(get("/admin/info")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(adminService, times(1)).getAccountInfoByEmail("test@example.com");
    }

    @Test
    void testGetUserInfoByEmail_NotFound() throws Exception {
        when(adminService.getAccountInfoByEmail("nonexistent@example.com")).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/admin/info")
                        .param("email", "nonexistent@example.com"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).getAccountInfoByEmail("nonexistent@example.com");
    }

    @Test
    void testActivateUserAccount_Success() throws Exception {
        doNothing().when(adminService).changeAccountStatus("activate", 1L);

        mockMvc.perform(put("/admin/activate/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account status changed successfully"));

        verify(adminService, times(1)).changeAccountStatus("activate", 1L);
    }

    @Test
    void testActivateUserAccount_NotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(adminService).changeAccountStatus("activate", 999L);

        mockMvc.perform(put("/admin/activate/999"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).changeAccountStatus("activate", 999L);
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
                .andExpect(status().isUnauthorized())  // Expecting 401 Unauthorized response
                .andExpect(content().string("Unauthorized"));  // Expecting unauthorized message

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


}