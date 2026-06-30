package com.gjleon.controller;

import com.gjleon.domain.UserProfile;
import com.gjleon.mapper.UserMapper;
import com.gjleon.mapper.UserProfileMapper;
import com.gjleon.response.UserProfileGetResponse;
import com.gjleon.response.UserProfileUserGetResponse;
import com.gjleon.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        log.info("Request received to profile with all users");

        var usersProfiles = service.findAll();
        var userProfileGetResponse = mapper.toUserProfileGetResponse(usersProfiles);

        return ResponseEntity.ok(userProfileGetResponse);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAll(@PathVariable Long id) {
        log.info("Request received to list all users by profile id '{}'", id);

        var users = service.findAllUsersByProfileId(id);
        var userProfileGetResponse = mapper.toUserProfileUserGetResponse(users);

        return ResponseEntity.ok(userProfileGetResponse);
    }
}
