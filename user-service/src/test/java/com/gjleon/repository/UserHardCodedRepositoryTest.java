package com.gjleon.repository;

import com.gjleon.cammons.UserUtils;
import com.gjleon.domain.User;
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
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;
    @Mock
    private UserData userData;
    private List<User> userList;
    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);
        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns an user when given id")
    void findById_ReturnsUserById_WhenSuccessful() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);

        var expectedUser = userList.getFirst();
        var foundUser = repository.findById(expectedUser.getId());

        Assertions.assertThat(foundUser).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns a empty list when firstName is null")
    void findByName_ReturnsEmptyList_WhenFirstNameIsNull() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);

        var user = repository.findByFirstName(null);
        Assertions.assertThat(user).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when firstName exist")
    void findByName_ReturnsFoundUserInList_WhenFirstNameExist() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);
        var expectedUser = userList.getFirst();
        var foundUser = repository.findByFirstName(expectedUser.getFirstName());

        Assertions.assertThat(foundUser).hasSize(1).contains(expectedUser);
    }

    @Test
    @DisplayName("save creates an user")
    void save_CreatesUser_WhenSuccessful() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);

        var userToSave = userUtils.newUserToSave();
        var user = repository.save(userToSave);

        Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var userSavedOptional = repository.findById(user.getId());

        Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete removes an user")
    void delete_RemoveUser_WhenSuccessful() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);

        var userToDelete = userList.getFirst();
        repository.delete(userToDelete);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates an user")
    void update_UpdatesUser_WhenSuccessful() {
        BDDMockito.given(userData.getUsers()).willReturn(userList);

        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Naruto");

        repository.update(userToUpdate);

        Assertions.assertThat(this.userList).contains(userToUpdate);

        var userUpdatedOptional = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdatedOptional).isPresent();
        Assertions.assertThat(userUpdatedOptional.get().getFirstName()).isEqualTo("Naruto");
    }
}