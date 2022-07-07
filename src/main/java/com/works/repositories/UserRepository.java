package com.works.repositories;

import com.works.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailEqualsAndPasswordEquals(String email, String password );
    List<User> findByNameContainsOrSurnameContainsAllIgnoreCase(String name, String surname);
    @Transactional
    @Modifying
    @Query("update User u set u.name = ?1 where u.uid = ?2")
    int updateNameByUid(String name, int uid);
}