package com.example.movierental.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileStore<T> {

    private final Path file;
    private final ObjectMapper mapper;
    private final TypeReference<List<T>> listType;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FileStore(Path file, ObjectMapper mapper, TypeReference<List<T>> listType) {
        this.file = file;
        this.mapper = mapper;
        this.listType = listType;
        try {
            Files.createDirectories(file.getParent());
            if (!Files.exists(file)) {
                Files.writeString(file, "[]");
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize file store: " + file, e);
        }
    }

    public List<T> readAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(mapper.readValue(file.toFile(), listType));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + file, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void writeAll(List<T> items) {
        lock.writeLock().lock();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), items);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + file, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
