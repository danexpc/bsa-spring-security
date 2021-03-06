package com.binarystudio.academy.springsecurity.domain.user;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public UserRepository(PasswordEncoder passwordEncoder) {
        var regularUser = new User();
        regularUser.setUsername("regular");
        regularUser.setEmail("regular@mail.com");
        regularUser.setId(UUID.fromString("ac7dec79-68bc-4fcb-a611-b1672726ca97"));
        regularUser.setPassword(passwordEncoder.encode("password"));
        regularUser.setAuthorities(Set.of(UserRole.USER));
        this.users.add(regularUser);

        var regular2User = new User();
        regular2User.setUsername("regular2");
        regular2User.setEmail("regular2@mail.com");
        regular2User.setId(UUID.randomUUID());
        regular2User.setPassword(passwordEncoder.encode("password2"));
        regular2User.setAuthorities(Set.of(UserRole.USER));
        this.users.add(regular2User);

        var adminUser = new User();
        adminUser.setUsername("privileged");
        adminUser.setEmail("privileged@mail.com");
        adminUser.setId(UUID.randomUUID());
        adminUser.setPassword(passwordEncoder.encode("password"));
        adminUser.setAuthorities(Set.of(UserRole.ADMIN));
        this.users.add(adminUser);
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findAny();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findAny();
    }

    public List<User> findUsers() {
        return Collections.unmodifiableList(users);
    }

    public void createUserByEmail(String email) {
        var createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setUsername(email);
        createdUser.setAuthorities(Set.of(UserRole.USER));
        users.add(createdUser);
    }

    public User createUser(String login, String email, String password) {
        var createdUser = new User();
        createdUser.setId(UUID.randomUUID());
        createdUser.setEmail(email);
        createdUser.setUsername(login);
        createdUser.setPassword(password);
        createdUser.setAuthorities(Set.of(UserRole.USER));
        users.add(createdUser);

        return createdUser;
    }

    public Optional<User> updatePassword(User user, String newPassword) {

        if (users.contains(user)) {
            var index = users.indexOf(user);
            var updatedUser = users.get(index);
            updatedUser.setPassword(newPassword);

            return Optional.of(updatedUser);
        }

        return Optional.empty();
    }

    public Optional<User> getByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
}
