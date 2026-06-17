package com.gjleon.service;

import com.gjleon.commons.ProfileUtils;
import com.gjleon.domain.Profile;
import com.gjleon.repository.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;
    @Mock
    private ProfileRepository repository;
    private List<Profile> profileList;
    @InjectMocks
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("findAll returns a list with all profiles")
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.findAll();
        Assertions.assertThat(profiles).isNotNull().hasSameElementsAs(profileList);
    }

    @Test
    @DisplayName("save creates an profile")
    void save_CreatesProfile_WhenSuccessful() {
        var profileToSave = profileUtils.newProfileToSave();
        var profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileSaved);

        var savedProfile = service.save(profileToSave);

        Assertions.assertThat(savedProfile).isEqualTo(profileSaved).hasNoNullFieldsOrProperties();
    }
}