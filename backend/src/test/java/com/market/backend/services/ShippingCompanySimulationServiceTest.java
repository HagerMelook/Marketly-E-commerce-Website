package com.market.backend.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import com.market.backend.models.Order;
import com.market.backend.repositories.OrderRepositories;





public class ShippingCompanySimulationServiceTest {

    @Mock
    private OrderRepositories orderRepositories;

    @InjectMocks
    private ShippingCompanySimulationService shippingCompanySimulationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderStatus_OrderNotFound() {
        Long orderId = 1L;
        when(orderRepositories.findById(orderId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = shippingCompanySimulationService.getOrderStatus(orderId);

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void testGetOrderStatus_OrderFound() {
        Long orderId = 1L;
        Order order = new Order();
        order.setDateTime(LocalDateTime.now().minusMinutes(10));
        when(orderRepositories.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepositories.save(any(Order.class))).thenReturn(order);

        ResponseEntity<?> response = shippingCompanySimulationService.getOrderStatus(orderId);

        assertEquals(ResponseEntity.ok("confirmed"), response);
    }

    @Test
    public void testGetListOrderStatus() {
        Order order1 = new Order();
        order1.setDateTime(LocalDateTime.now().minusMinutes(10));
        Order order2 = new Order();
        order2.setDateTime(LocalDateTime.now().minusMinutes(50));
        Slice<Order> orderSlice = new SliceImpl<>(Arrays.asList(order1, order2), PageRequest.of(0, 2), false);
        when(orderRepositories.save(any(Order.class))).thenReturn(order1).thenReturn(order2);

        ResponseEntity<Slice<Order>> response = shippingCompanySimulationService.getListOrderStatus(orderSlice);

        assertEquals(2, response.getBody().getContent().size());
        assertEquals("confirmed", response.getBody().getContent().get(0).getStatus());
        assertEquals("processing", response.getBody().getContent().get(1).getStatus());
    }

    @Test
    public void testUpdateOrderStatus() {
        Order order = new Order();
        order.setDateTime(LocalDateTime.now().minusMinutes(10));
        when(orderRepositories.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = shippingCompanySimulationService.updateOrderStatus(order);

        assertEquals("confirmed", updatedOrder.getStatus());
    }
}