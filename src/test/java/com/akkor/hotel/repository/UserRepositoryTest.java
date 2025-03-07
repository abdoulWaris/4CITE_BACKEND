package com.akkor.hotel.repository;

import com.akkor.hotel.model.Role;
import com.akkor.hotel.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@TestPropertySource(properties = "SPRING_APPLICATION_NAME=TestApp")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
@BeforeEach
    public void setUp() {
        // Créer un utilisateur de test
        User user = User.builder()
                .email("robert.brown@example.com")
                .enabled(true)
                .firstName("Robert")
                .lastName("Brown")
                .password("welcome123")
                .phoneNumber("123456789")
                .roles(Set.of(Role.ROLE_USER)) // Ajouter un rôle
                .build();
    }

    @Test
    void shouldGetAllPersonne() {
        List<User> usersList = userRepository.findAll();
        assertEquals(4, usersList.size());
        assertEquals("jane.smith@example.com",usersList.get(1).getEmail());
    }

    @Test
    void shouldGetUserById(){
        User user1 = userRepository.findById(1L).get();
        User user4 = userRepository.findById(3L).get();

        assertEquals("John",user1.getFirstName());
        assertEquals("Mike",user4.getFirstName());

    }

    @Test
    void shouldUpdateUser(){
        User findedUser = userRepository.findById(4L).get();
        findedUser.setPhoneNumber("987654321");
        User user = userRepository.save(findedUser);
        assertEquals("987654321", user.getPhoneNumber());
    }

    @Test
    void shouldDeleteUser(){
    userRepository.deleteById(4L);
    Optional<User> user = userRepository.findById(4L);

    assertFalse(user.isPresent());

    }
}
