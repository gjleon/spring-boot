package com.gjleon.controller;

import com.gjleon.mapper.ProfileMapper;
import com.gjleon.request.ProfilePostRequest;
import com.gjleon.response.ProfileGetResponse;
import com.gjleon.response.ProfilePostResponse;
import com.gjleon.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        log.debug("Request received to list all profiles");

        var profiles = service.findAll();

        var profileGetResponses = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(profileGetResponses);
    }


    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {
        log.debug("Request to save profile : {}", request);

        var profile = mapper.toProfile(request);

        var profileSaved = service.save(profile);

        var profilePostResponse = mapper.toProfilePostResponse(profileSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(profilePostResponse);
    }
}
