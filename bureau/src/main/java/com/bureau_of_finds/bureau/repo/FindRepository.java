package com.bureau_of_finds.bureau.repo;

import com.bureau_of_finds.bureau.models.Find;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindRepository extends CrudRepository<Find, Long> {

    Optional<Find> findByIdAndUserId(Long id, Long userId);
    @EntityGraph(attributePaths = "image")
    Optional<Find> findById(Long id);

    Iterable<Find> findAllByDateOfCreatedAfter(LocalDateTime a);
}

