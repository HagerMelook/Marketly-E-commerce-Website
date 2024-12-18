package com.market.backend.repositories;

import org.springframework.data.jpa.repository.Query;
import com.market.backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAccountId(Long id);
    List<Comment> findByProductId(Long id);
    @Query("SELECT AVG(e.rating) FROM Comment e")
    Double findAverageValue();
}
