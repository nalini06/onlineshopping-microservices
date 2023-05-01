package com.example.demo.service;


import com.example.demo.dto.OrderLineItemsDto;
import com.example.demo.dto.OrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderLineItems;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.OrderRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .build();
        System.out.println(String.valueOf(order));
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemListDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);
        try{
            orderRepository.save(order);
        }catch (Exception exception){
            log.error("Failed to save order in db " + exception.getMessage());
        }

        return order;
    }

    public OrderLineItems mapToDto(OrderLineItemsDto itemsDto){
        OrderLineItems orderLineItems = OrderLineItems.builder()
                .price(itemsDto.getPrice())
                .quantity(itemsDto.getQuantity())
                .skuCode(itemsDto.getSkuCode())
                .build();
        return orderLineItems;
    }

    public List<Order> getAllProducts(){
        List<Order> productResponses = orderRepository.findAll();
        return productResponses;
    }

}
