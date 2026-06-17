package com.gjleon.service;

import com.gjleon.domain.User;
import com.gjleon.exception.EmailAlreadyExistException;
import com.gjleon.exception.NotFoundException;
import com.gjleon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User userToSave) {
        assertEmailDoesNotExist(userToSave.getEmail());
        return repository.save(userToSave);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate.getId());
        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());
        repository.save(userToUpdate);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    private void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailExistException);
    }

    private void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailExistException);
    }

    private void throwEmailExistException(User user) {
        throw new EmailAlreadyExistException("Email %s already exists".formatted(user.getEmail()));
    }

}
