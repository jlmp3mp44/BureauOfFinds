package com.bureau_of_finds.bureau.repo;



import com.bureau_of_finds.bureau.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
