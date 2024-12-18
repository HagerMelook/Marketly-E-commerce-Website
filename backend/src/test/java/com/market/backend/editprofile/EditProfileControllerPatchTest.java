package com.market.backend.editprofile;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.market.backend.controllers.EditProfileController;
import com.market.backend.services.EditProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EditProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EditProfileControllerPatchTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private EditProfileService editProfileService;

    @Test
    void updateAdminInfoValidInputStatus() throws Exception {
        // Arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;


        Mockito.doNothing().when(editProfileService).updateAdminInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/admininfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isOk());
    }

    @Test
    void updateAdminInfoInvalidInputFormat() throws Exception {
        // Arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"wrong"}
                ]
                """;


        Mockito.doThrow(JsonPatchException.class).when(editProfileService).updateAdminInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/admininfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAdminInfoInvalidId() throws Exception {
        // Arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;


        Mockito.doThrow(NoSuchElementException.class).when(editProfileService).updateAdminInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/admininfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateClientInfoValidInputStatus() throws Exception {
        // Arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;


        Mockito.doNothing().when(editProfileService).updateClientInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/clientinfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isOk());
    }

    @Test
    void updateClientInfoInvalidInputFormat() throws Exception {
        // Arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"wrong"}
                ]
                """;


        Mockito.doThrow(JsonPatchException.class).when(editProfileService).updateClientInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/clientinfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateClientInfoInvalidId() throws Exception {
        // Arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;


        Mockito.doThrow(NoSuchElementException.class).when(editProfileService).updateClientInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/clientinfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateVendorInfoValidInputStatus() throws Exception {
        // Arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;


        Mockito.doNothing().when(editProfileService).updateVendorInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/vendorinfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isOk());
    }


    @Test
    void updateVendorInfoInvalidInputFormat() throws Exception {
        // Arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"wrong"}
                ]
                """;


        Mockito.doThrow(JsonPatchException.class).when(editProfileService).updateVendorInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/vendorinfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCVendorInfoInvalidId() throws Exception {
        // Arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;


        Mockito.doThrow(NoSuchElementException.class).when(editProfileService).updateVendorInfo(Mockito.eq(id), Mockito.any(JsonPatch.class));

        // Act and Assert
        mockMvc.perform(patch("/account/vendorinfo/{id}", id)
                        .contentType("application/json-patch+json")
                        .content(patchContent))
                .andExpect(status().isNotFound());
    }
}
