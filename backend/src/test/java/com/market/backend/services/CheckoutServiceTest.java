package com.market.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.dtos.OrderDTO;
import com.market.backend.dtos.OrderProductDTO;
import com.market.backend.models.Account;
import com.market.backend.models.Order;
import com.market.backend.models.OrderProducts;
import com.market.backend.models.OrderProductsId;
import com.market.backend.models.Product;
import com.market.backend.models.ShippingInfo;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.OrderProductsRepository;
import com.market.backend.repositories.OrderRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.ShippingInfoRepository;

@SpringBootTest
public class CheckoutServiceTest {

    @InjectMocks
    private CheckoutService checkoutService;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private ShippingInfoRepository shippingInfoRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private OrderProductsRepository orderProductsRepo;

    @Test
    void testInsertShippingInfoAccountNotExist() {
        when(accountRepo.existsById(1L)).thenReturn(false);
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setId(1L);
        String result = checkoutService.insertShippingInfo(shippingInfo);
        assertEquals("Something Went Wrong, Please Try Again", result);
    }

    @Test
    void testInsertShippingInfoShippingInfoExist() {
        when(accountRepo.existsById(1L)).thenReturn(true);
        when(shippingInfoRepo.existsById(1L)).thenReturn(true);

        ShippingInfo shippingInfoOld = new ShippingInfo();
        shippingInfoOld.setId(1L);
        shippingInfoOld.setAddress("alexandria");
        shippingInfoOld.setPhoneNumber("201003889701");
        shippingInfoOld.setPostalCode("11511");

        ShippingInfo shippingInfoNew = new ShippingInfo();
        shippingInfoNew.setId(1L);
        shippingInfoNew.setAddress("alexandria");
        shippingInfoNew.setPhoneNumber("201003889702");
        shippingInfoNew.setPostalCode("11511");

        when(shippingInfoRepo.findById(1L)).thenReturn(Optional.of(shippingInfoOld));
        String result = checkoutService.insertShippingInfo(shippingInfoNew);
        assertEquals("Shipping Info Updated Successfully", result);
        assertEquals("201003889702", shippingInfoOld.getPhoneNumber());
        verify(shippingInfoRepo).save(shippingInfoNew);
    }

    @Test
    void testInsertShippingInfoShippingInfoNotExist() {
        when(accountRepo.existsById(1L)).thenReturn(true);
        when(shippingInfoRepo.existsById(1L)).thenReturn(false);

        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setId(1L);
        shippingInfo.setAddress("alexandria");
        shippingInfo.setPhoneNumber("201003889701");
        shippingInfo.setPostalCode("11511");

        String result = checkoutService.insertShippingInfo(shippingInfo);
        assertEquals("Shipping Info Inserted Successfully", result);
        verify(shippingInfoRepo).save(shippingInfo);
    }

    @Test
    void testInsertOrderDetailsAccountNotExist() {
        OrderDTO order = OrderDTO.builder()
                .account_id(1L)
                .build();

        when(accountRepo.existsById(1L)).thenReturn(false);
        String result = checkoutService.insertOrderDetails(order);
        assertEquals("Something Went Wrong, Please Try Again", result);
    }

    @Test
    void testInsertOrderDetailsProductNotExist() {
        OrderProductDTO order_product = OrderProductDTO.builder()
                .id(1L)
                .name("Earbuds")
                .price(300.0)
                .quantity(2)
                .build();

        List<OrderProductDTO> order_products = new ArrayList<>();
        order_products.add(order_product);

        OrderDTO orderDTO = OrderDTO.builder()
                .account_id(1L)
                .checkoutPrice(300)
                .order_products(order_products)
                .build();

        Account account = Account.builder()
                .id(1L).build();

        when(accountRepo.existsById(1L)).thenReturn(true);
        when(productRepo.existsById(1L)).thenReturn(false);
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        String result = checkoutService.insertOrderDetails(orderDTO);
        assertEquals("Something Went Wrong, Please Try Again", result);
    }

    @Test
    void testInsertOrderDetailsProductExist() {
        OrderProductDTO order_product = OrderProductDTO.builder()
                .id(1L)
                .name("Earbuds")
                .price(300.0)
                .quantity(2)
                .build();

        List<OrderProductDTO> order_products = new ArrayList<>();
        order_products.add(order_product);

        OrderDTO orderDTO = OrderDTO.builder()
                .account_id(1L)
                .checkoutPrice(300)
                .order_products(order_products)
                .build();

        Account account = Account.builder()
                .id(1L).build();

        Product product = Product.builder()
                .id(1L).build();

        Order order = Order.builder()
                .id(1L)
                .account(account)
                .checkoutPrice(orderDTO.getCheckoutPrice())
                .status("processing")
                .date(LocalDateTime.now())
                .build();

        OrderProductsId orderProductsId = new OrderProductsId(order.getId(), product.getId());
        OrderProducts orderProducts = new OrderProducts();
        orderProducts.setOrderProductsId(orderProductsId);
        orderProducts.setOrder(order);
        orderProducts.setProduct(product);

        when(accountRepo.existsById(1L)).thenReturn(true);
        when(productRepo.existsById(1L)).thenReturn(true);
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        
        String result = checkoutService.insertOrderDetails(orderDTO);
        assertEquals("Order is Registered Successfully," + order.getId(), result);

        ArgumentCaptor<OrderProducts> captor = ArgumentCaptor.forClass(OrderProducts.class);
        verify(orderProductsRepo).save(captor.capture());
        OrderProducts capturedOrderProducts = captor.getValue();
        assertEquals(order.getId(), capturedOrderProducts.getOrder().getId());
        assertEquals(product.getId(), capturedOrderProducts.getProduct().getId());
        assertEquals(order_product.getQuantity(), capturedOrderProducts.getQuantity());
    }

    @Test
    void testGetShippingInfoAccountNotExist() {
        ShippingInfo shippingInfo = new ShippingInfo();
        when(accountRepo.existsById(1L)).thenReturn(false);
        ShippingInfo result = checkoutService.getShippingInfoIfExist(1L);
        assertEquals(shippingInfo, result);
    }

    @Test
    void testGetShippingInfoShippingInfoNotExist() {
        ShippingInfo shippingInfo = new ShippingInfo();
        when(accountRepo.existsById(1L)).thenReturn(true);
        when(shippingInfoRepo.existsById(1L)).thenReturn(false);
        ShippingInfo result = checkoutService.getShippingInfoIfExist(1L);
        assertEquals(shippingInfo, result);
    }

    @Test
    void testGetShippingInfoShippingInfoExist() {
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setId(1L);
        shippingInfo.setAddress("alexandria");
        shippingInfo.setPhoneNumber("201003889701");
        shippingInfo.setPostalCode("11511");
        when(accountRepo.existsById(1L)).thenReturn(true);
        when(shippingInfoRepo.existsById(1L)).thenReturn(true);
        when(shippingInfoRepo.findById(1L)).thenReturn(Optional.of(shippingInfo));
        ShippingInfo result = checkoutService.getShippingInfoIfExist(1L);
        assertEquals(shippingInfo, result);
    }
}
