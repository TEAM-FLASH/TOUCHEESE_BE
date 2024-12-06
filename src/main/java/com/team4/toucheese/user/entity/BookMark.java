package com.team4.toucheese.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookMark {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long studioId;

    @CreationTimestamp
    private LocalDateTime createTime;

    @Builder
    public BookMark(Long userId, Long studioId) {
        this.userId = userId;
        this.studioId = studioId;
    }
}
