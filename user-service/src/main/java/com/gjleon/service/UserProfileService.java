package com.gjleon.service;

import com.gjleon.domain.Profile;
import com.gjleon.domain.User;
import com.gjleon.domain.UserProfile;
import com.gjleon.exception.NotFoundException;
import com.gjleon.repository.ProfileRepository;
import com.gjleon.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.retrieveAll();
    }

    public List<User> findAllUsersByProfileId(Long id) {
        return repository.findAllUsersByProfileId(id);
    }
}
