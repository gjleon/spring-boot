package com.gjleon.controller;

import com.gjleon.domain.UserProfile;
import com.gjleon.mapper.UserMapper;
import com.gjleon.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll(@RequestParam(required = false) String firstName) {
        log.info("Request received to list all user profiles");

        var usersProfiles = service.findAll();

        return ResponseEntity.ok(usersProfiles);
    }
}
