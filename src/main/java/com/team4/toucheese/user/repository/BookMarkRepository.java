package com.team4.toucheese.user.repository;

import com.team4.toucheese.user.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Optional<Object> findByIdAndStudioId(Long id, Long studioId);

    Optional<Object> findByUserIdAndStudioId(Long userId, Long studioId);
}
