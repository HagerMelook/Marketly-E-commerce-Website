package com.market.backend.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.backend.configurations.JWTFilter;
import com.market.backend.dtos.OrderDTO;
import com.market.backend.models.ShippingInfo;
import com.market.backend.services.CheckoutService;
import com.market.backend.services.JWTService;

@AutoConfigureMockMvc(addFilters = false)
@Import({ JWTService.class, JWTFilter.class })
@WebMvcTest(CheckoutController.class)
public class CheckoutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private CheckoutService checkoutService;

    @Test
    void testCheckoutOrderDetailsValid() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder().build();
        String body = new ObjectMapper().writeValueAsString(orderDTO);
        String expectedMsg = "Order is Registered Successfully,1";
        when(checkoutService.insertOrderDetails(orderDTO)).thenReturn(expectedMsg);
        mockMvc.perform(post("/checkout/orderDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMsg));
    }

    @Test
    void testCheckoutOrderDetailsNotValid() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder().build();
        String body = new ObjectMapper().writeValueAsString(orderDTO);
        String expectedMsg = "Something Went Wrong, Please Try Again";
        when(checkoutService.insertOrderDetails(orderDTO)).thenReturn(expectedMsg);
        mockMvc.perform(post("/checkout/orderDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMsg));
    }

    @Test
    void testCheckoutShippingInfoNotValid() throws Exception {
        ShippingInfo shippingInfo = new ShippingInfo();
        String body = new ObjectMapper().writeValueAsString(shippingInfo);
        String expectedMsg = "Something Went Wrong, Please Try Again";
        when(checkoutService.insertShippingInfo(shippingInfo)).thenReturn(expectedMsg);
        mockMvc.perform(post("/checkout/shippingInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMsg));
    }
    @Test
    void testCheckoutShippingInfoUpdated() throws Exception {
        ShippingInfo shippingInfo = new ShippingInfo();
        String body = new ObjectMapper().writeValueAsString(shippingInfo);
        String expectedMsg = "Shipping Info Updated Successfully";
        when(checkoutService.insertShippingInfo(shippingInfo)).thenReturn(expectedMsg);
        mockMvc.perform(post("/checkout/shippingInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMsg));
    }
    @Test
    void testCheckoutShippingInfoInserted() throws Exception {
        ShippingInfo shippingInfo = new ShippingInfo();
        String body = new ObjectMapper().writeValueAsString(shippingInfo);
        String expectedMsg = "Shipping Info Inserted Successfully";
        when(checkoutService.insertShippingInfo(shippingInfo)).thenReturn(expectedMsg);
        mockMvc.perform(post("/checkout/shippingInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMsg));

    }

    @Test
    void testGetShippingInfoExist() throws Exception {
        ShippingInfo shippingInfo = new ShippingInfo();
        Mockito.when(checkoutService.getShippingInfoIfExist(1L)).thenReturn(shippingInfo);
        mockMvc.perform(get("/checkout/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.phoneNumber").isEmpty())
                .andExpect(jsonPath("$.postalCode").isEmpty())
                .andExpect(jsonPath("$.address").isEmpty());
    }

    @Test
    void testGetShippingInfoNotExist() throws Exception {
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setId(1L);
        shippingInfo.setAddress("alexandria");
        shippingInfo.setPhoneNumber("201003889701");
        shippingInfo.setPostalCode("11511");
        Mockito.when(checkoutService.getShippingInfoIfExist(1L)).thenReturn(shippingInfo);
        mockMvc.perform(get("/checkout/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.phoneNumber").value("201003889701"))
                .andExpect(jsonPath("$.postalCode").value("11511"))
                .andExpect(jsonPath("$.address").value("alexandria"));
    }
}
