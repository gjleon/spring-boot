package com.gjleon.controller;

import com.gjleon.config.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/connections")
@RequiredArgsConstructor
public class ConnectionController {
    private  final Connection connectionMySql;

    @GetMapping
    public ResponseEntity<Connection> getConnection() {
        return ResponseEntity.ok(connectionMySql);
    }
}
