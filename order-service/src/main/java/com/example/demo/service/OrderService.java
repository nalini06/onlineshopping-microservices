package com.example.demo.service;


import com.example.demo.dto.InventoryResponse;
import com.example.demo.dto.OrderLineItemsDto;
import com.example.demo.dto.OrderRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderLineItems;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import com.example.demo.repository.OrderRepository;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Arrays;
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
    private  WebClient.Builder webClient;

    @Autowired
    private Tracer tracer;

    public String placeOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .build();

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemListDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        Span inventoryLookUp = tracer.nextSpan().name("InventoryServiceLookup");

        try( Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryLookUp.start())){
            InventoryResponse[] inventoryResponses = webClient.build().get()
                    .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

            if(allProductsInStock){
                orderRepository.save(order);
            }else{
                log.error("Failed to save order in db ");
                throw  new IllegalArgumentException("Product is not available in inventory");
            }
            return "Order Placed successfully!!";
        }finally {
                inventoryLookUp.end();
        }
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
