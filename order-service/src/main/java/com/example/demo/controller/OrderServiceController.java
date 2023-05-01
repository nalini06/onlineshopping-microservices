package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderServiceController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody OrderRequest orderRequest){
        Order placedOrder = orderService.placeOrder(orderRequest);
        return placedOrder;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Order> getOrder(){
        return orderService.getAllProducts();
    }

}