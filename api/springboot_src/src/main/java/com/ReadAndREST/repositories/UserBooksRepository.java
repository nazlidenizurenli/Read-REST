package com.ReadAndREST.repositories;

import com.ReadAndREST.models.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBooksRepository extends JpaRepository<UserBooks, Long> {
}
