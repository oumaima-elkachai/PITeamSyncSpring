package com.teamsync.userpi.repository;
import com.teamsync.userpi.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
@Repository
public interface  UserRepository extends MongoRepository<User, String> {

     Optional<User> findById(String id);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(@NotNull(message = "Email can't be null") String email);
    Optional<User> findByResetToken(String token);

}
