package com.team4.toucheese.review.entity;

import com.team4.toucheese.studio.entity.AdditionalOption;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<AdditionalOption> additionalOptions;

    private String content;
    private Integer rating;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

    @Column(unique = true)
    private Long reservationId;

}
