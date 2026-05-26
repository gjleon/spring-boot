package com.gjleon.controller;

import com.gjleon.mapper.UserMapper;
import com.gjleon.request.UserPostRequest;
import com.gjleon.request.UserPutRequest;
import com.gjleon.response.UserGetResponse;
import com.gjleon.response.UserPostResponse;
import com.gjleon.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        var usersList = service.findAll(firstName);
        var userGetResponse = mapper.toUserGetResponseList(usersList);

        return ResponseEntity.ok(userGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        var userFound = service.findByIdOrThrowNotFound(id);
        var userGetResponse = mapper.toUserGetResponseList(userFound);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest request) {
        var userToSave = mapper.toUser(request);
        var userSave = service.save(userToSave);

        var userPostResponse = mapper.toUserPostResponse(userSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
        var userToUpdate = mapper.toUser(request);
        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }
}
