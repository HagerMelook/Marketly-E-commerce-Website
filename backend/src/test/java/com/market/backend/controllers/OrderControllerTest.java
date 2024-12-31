package com.market.backend.controllers;

import com.market.backend.configurations.JWTFilter;
import com.market.backend.dtos.OrderDTO;
import com.market.backend.models.Order;
import com.market.backend.services.JWTService;
import com.market.backend.services.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({JWTService.class, JWTFilter.class})
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    static List<OrderDTO> orders;

    @BeforeAll
    static void setUp() throws Exception {
        orders = Arrays.asList(
                OrderDTO.builder()
                        .id(1l)
                        .date(new Date(12, 12, 2026))
                        .checkoutPrice(1000)
                        .status("confirmd")
                        .build()
        );
    }

    @Test
    void allOrdersRetrieval_ReturnsOk200() throws Exception {
        when(orderService.listAllOrders(1)).thenReturn(orders);

        mockMvc.perform(get("/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void allOrdersRetrieval_ReturnsBadRequest() throws Exception {
        when(orderService.listAllOrders(1)).thenReturn(orders);

        mockMvc.perform(get("/orders/{id}", "ahmed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrders_ReturnsOk200() throws Exception {
        doNothing().when(orderService).cancelOrder(1);

        mockMvc.perform(delete("/orders/cancel/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrders_ReturnsBadRequest() throws Exception {
        doNothing().when(orderService).cancelOrder(1);

        mockMvc.perform(delete("/orders/{id}", "ahmed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(405));
    }


}
