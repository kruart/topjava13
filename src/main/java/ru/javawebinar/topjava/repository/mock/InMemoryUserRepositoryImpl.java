package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private Set<User> users = new HashSet<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private Comparator<User> userComparator = (u1, u2) -> u1.getName().compareTo(u2.getName());

    {
        save(new User(null, "Admin", "admin@gmail.com", "12345", Role.ROLE_ADMIN));
        save(new User(null, "User", "user@gmail.com", "12345", Role.ROLE_USER));
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            users.add(user);
        } else if(get(user.getId()) != null) {
            users.remove(user);
            users.add(user);
        }

        return user;
    }

    @Override
    public boolean delete(int id) {
        return users.remove(get(id));
    }

    @Override
    public User get(int id) {
        return users.stream()
                .filter(u -> id == u.getId())
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return users.stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        return users.stream()
                .sorted(userComparator)
                .collect(Collectors.toList());
    }
}
