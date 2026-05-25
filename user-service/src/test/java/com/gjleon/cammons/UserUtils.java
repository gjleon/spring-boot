package com.gjleon.cammons;

import com.gjleon.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        var nicolas = User.builder().id(1L).firstName("Nicolas").lastName("Natal").email("nicolas@hotmail.com").build();
        var wesley = User.builder().id(2L).firstName("Wesley").lastName("Marques").email("wesley@yahoo.com").build();
        var isabella = User.builder().id(3L).firstName("Isabella").lastName("Azevedo").email("isabella@uol.com").build();


        return new ArrayList<>(List.of(nicolas, wesley, isabella));
    }

    public User newUserToSave() {
        return User.builder().id(99L).firstName("Roberval").lastName("Junkeira").email("rjunkeira@tuamae.com").build();
    }
}
