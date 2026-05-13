package com.example.movierental.repository;

import com.example.movierental.entity.User;
import com.example.movierental.storage.FileStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final FileStore<User> store;

    public UserRepository(@Value("${app.data.dir:./data}") String dataDir, ObjectMapper mapper) {
        this.store = new FileStore<>(Path.of(dataDir, "users.json"), mapper, new TypeReference<>() {});
    }

    public User save(User user) {
        List<User> all = store.readAll();
        if (user.getId() == null) {
            long nextId = all.stream().mapToLong(u -> u.getId() == null ? 0L : u.getId()).max().orElse(0L) + 1;
            user.setId(nextId);
            all.add(user);
        } else {
            all.replaceAll(u -> u.getId().equals(user.getId()) ? user : u);
        }
        store.writeAll(all);
        return user;
    }

    public List<User> findAll() {
        return store.readAll();
    }

    public Optional<User> findById(Long id) {
        return store.readAll().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return store.readAll().stream().filter(u -> email.equals(u.getEmail())).findFirst();
    }

    public boolean existsByEmail(String email) {
        return store.readAll().stream().anyMatch(u -> email.equals(u.getEmail()));
    }

    public void delete(User user) {
        List<User> all = store.readAll();
        all.removeIf(u -> u.getId().equals(user.getId()));
        store.writeAll(all);
    }
}
