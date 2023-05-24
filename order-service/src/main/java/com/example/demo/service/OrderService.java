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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private  WebClient webClient;

    public Order placeOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .build();

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemListDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);

        Boolean result = webClient.get()
                .uri("http://localhost:8081/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(result){
            orderRepository.save(order);
        }else{

            log.error("Failed to save order in db ");
            throw  new IllegalArgumentException("Product is not available in inventory");
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
