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
public void testGetOrderStatus_OrderDelivered() {
    Long orderId = 1L;
    Order order = new Order();
    order.setDate(LocalDateTime.now().minusMinutes(5));
    when(orderRepositories.findById(orderId)).thenReturn(Optional.of(order));
    when(orderRepositories.save(any(Order.class))).thenReturn(order);

    ResponseEntity<?> response = shippingCompanySimulationService.getOrderStatus(orderId);

    assertEquals(ResponseEntity.ok("delivered"), response);
}

@Test
public void testGetOrderStatus_OrderShipped() {
    Long orderId = 1L;
    Order order = new Order();
    order.setDate(LocalDateTime.now().minusMinutes(3));
    when(orderRepositories.findById(orderId)).thenReturn(Optional.of(order));
    when(orderRepositories.save(any(Order.class))).thenReturn(order);

    ResponseEntity<?> response = shippingCompanySimulationService.getOrderStatus(orderId);

    assertEquals(ResponseEntity.ok("shipped"), response);
}

@Test
public void testGetOrderStatus_OrderPackaged() {
    Long orderId = 1L;
    Order order = new Order();
    order.setDate(LocalDateTime.now().minusMinutes(2));
    when(orderRepositories.findById(orderId)).thenReturn(Optional.of(order));
    when(orderRepositories.save(any(Order.class))).thenReturn(order);

    ResponseEntity<?> response = shippingCompanySimulationService.getOrderStatus(orderId);

    assertEquals(ResponseEntity.ok("packaged"), response);
}

@Test
public void testGetOrderStatus_OrderProcessing() {
    Long orderId = 1L;
    Order order = new Order();
    order.setDate(LocalDateTime.now().minusMinutes(1));
    when(orderRepositories.findById(orderId)).thenReturn(Optional.of(order));
    when(orderRepositories.save(any(Order.class))).thenReturn(order);

    ResponseEntity<?> response = shippingCompanySimulationService.getOrderStatus(orderId);

    assertEquals(ResponseEntity.ok("processing"), response);
}

@Test
public void testGetListOrderStatus_ListWithMultipleOrders() {
    Order order1 = new Order();
    order1.setDate(LocalDateTime.now().minusMinutes(1));
    Order order2 = new Order();
    order2.setDate(LocalDateTime.now().minusMinutes(3));
    Order order3 = new Order();
    order3.setDate(LocalDateTime.now().minusMinutes(5));
    Slice<Order> orderSlice = new SliceImpl<>(Arrays.asList(order1, order2, order3), PageRequest.of(0, 3), false);
    when(orderRepositories.save(any(Order.class))).thenReturn(order1).thenReturn(order2).thenReturn(order3);

    Slice<Order> response = shippingCompanySimulationService.getListOrderStatus(orderSlice);

    assertEquals(3, response.getContent().size());
    assertEquals("processing", response.getContent().get(0).getStatus());
    assertEquals("shipped", response.getContent().get(1).getStatus());
    assertEquals("delivered", response.getContent().get(2).getStatus());
}}