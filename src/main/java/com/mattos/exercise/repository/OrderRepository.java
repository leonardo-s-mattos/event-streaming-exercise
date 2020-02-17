package com.mattos.exercise.repository;

import com.mattos.exercise.domain.Order;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class OrderRepository implements OrderMongoRepository {
    @Override
    public <S extends Order> Mono<S> insert(S s) {
        return null;
    }

    @Override
    public <S extends Order> Flux<S> insert(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends Order> Flux<S> insert(Publisher<S> publisher) {
        return null;
    }

    @Override
    public <S extends Order> Mono<S> findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Order> Flux<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Order> Flux<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Order> Mono<Long> count(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Order> Mono<Boolean> exists(Example<S> example) {
        return null;
    }

    @Override
    public Flux<Order> findAll(Sort sort) {
        return null;
    }

    @Override
    public <S extends Order> Mono<S> save(S s) {
        return null;
    }

    @Override
    public <S extends Order> Flux<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends Order> Flux<S> saveAll(Publisher<S> publisher) {
        return null;
    }

    @Override
    public Mono<Order> findById(String s) {
        return null;
    }

    @Override
    public Mono<Order> findById(Publisher<String> publisher) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(String s) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<String> publisher) {
        return null;
    }

    @Override
    public Flux<Order> findAll() {
        return null;
    }

    @Override
    public Flux<Order> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public Flux<Order> findAllById(Publisher<String> publisher) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String s) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Publisher<String> publisher) {
        return null;
    }

    @Override
    public Mono<Void> delete(Order order) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends Order> iterable) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends Order> publisher) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
