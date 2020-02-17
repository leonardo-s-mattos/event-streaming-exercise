package com.mattos.exercise.web;

import com.mattos.exercise.domain.Order;
import com.mattos.exercise.repository.OrderMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    @Autowired
    private OrderMongoRepository orderRepository;

    @PostMapping()
    public @ResponseBody
    Mono<Order> addOrder(@RequestBody Order kayak) {
        return orderRepository.save(kayak);
    }

    @GetMapping()
    public @ResponseBody
    Flux<Order> getAllOrders() {
        Flux<Order> result = orderRepository.findAll();
        return result;
    }

}