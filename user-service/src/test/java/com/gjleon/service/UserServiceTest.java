package com.gjleon.service;

import com.gjleon.cammons.UserUtils;
import com.gjleon.domain.User;
import com.gjleon.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    private List<User> userList;
    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns a list with found object when argument exist")
    void findAll_ReturnsUsers_WhenArgumentExist() {
        var user = userList.getFirst();

        var expectedUserFound = singletonList(user);
        BDDMockito.when(repository.findByFirstNameIgnoreCase(user.getFirstName())).thenReturn(expectedUserFound);

        var userFound = service.findAll(user.getFirstName());
        Assertions.assertThat(userFound).containsAll(expectedUserFound);
    }

    @Test
    @DisplayName("findAll returns empty list when argument not found")
    void findAll_ReturnsEmptyList_WhenArgumentNotFound() {
        var firstName = "not found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(emptyList());

        var users = repository.findByFirstNameIgnoreCase(firstName);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an user with given id")
    void findById_ReturnsUsersById_WhenSuccessful() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        var users = service.findByIdOrThrowNotFound(expectedUser.getId());

        Assertions.assertThat(users).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user not found")
    void findById_ThrowsResponseStatusException_WhenUserNotFound() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an user")
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();
        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);

        var savedUser = service.save(userToSave);

        Assertions.assertThat(savedUser).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete remove an user")
    void delete_RemoveUser_WhenSuccessful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when user not found")
    void delete_ThrowsResponseStatusException_WhenUserNotFound() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(userToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an user")
    void update_UpdatesUser_WhenSuccessful() {
        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Helsing");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        service.update(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user not found")
    void update_ThrowsResponseStatusException_WhenUserNotFound() {
        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Helsing");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}