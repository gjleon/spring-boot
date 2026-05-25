package com.gjleon.repository;

import com.gjleon.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    private final List<User> userList = new ArrayList<>();

    {
        var gabriel = User.builder().id(1L).firstName("Gabriel").lastName("Leon").email("gabriel@hotmail.com").build();
        var welker = User.builder().id(2L).firstName("Welker").lastName("Mascarenhas").email("welker@yahoo.com").build();
        var gilberto = User.builder().id(3L).firstName("Gilberto").lastName("Nascimento").email("gilberto@uol.com").build();

        userList.addAll(List.of(gabriel, welker, gilberto));
    }

    public List<User> getUsers() {
        return userList;
    }
}
