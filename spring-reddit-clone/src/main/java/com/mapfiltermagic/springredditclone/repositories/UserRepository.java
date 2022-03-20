package com.mapfiltermagic.springredditclone.repositories;

import java.util.Optional;

import com.mapfiltermagic.springredditclone.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

}
