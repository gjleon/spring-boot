package com.gjleon.controller;

import com.gjleon.mapper.ProducerMapper;
import com.gjleon.request.ProducerPostRequest;
import com.gjleon.request.ProducerPutRequest;
import com.gjleon.resonse.ProducerGetResponse;
import com.gjleon.resonse.ProducerPostResponse;
import com.gjleon.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/producers")
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerMapper mapper;
    private final ProducerService service;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received for list All producers, param name: '{}'", name);
        var producerList = service.findAll(name);
        var producerGetResponse = mapper.toProducerGetResponseList(producerList);

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);
        var response = service.findByIdOrThrowNotFound(id);
        var producerGetResponse = mapper.toProducerGetResponse(response);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        var producer = mapper.toProducer(producerPostRequest);

        var producerSaved = service.save(producer);

        var producerPostResponse = mapper.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        log.debug("Request to update anime: {}", request);
        var producerUpdate = mapper.toProducer(request);

        service.update(producerUpdate);

        return ResponseEntity.noContent().build();
    }
}
