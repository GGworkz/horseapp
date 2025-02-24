package com.horseapp.rest_demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonCrudService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public CommonCrudService(@Qualifier("personRepository") JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    public void delete(ID id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public T update(T entity) {
        return repository.save(entity);
    }
}
