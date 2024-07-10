package com.ReadAndREST.repositories;

import com.ReadAndREST.models.UserRecommendedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecommendedBooksRepository extends JpaRepository<UserRecommendedBooks, Long> {
}
