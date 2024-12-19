package com.team4.toucheese.studio.entity;

import com.team4.toucheese.vibe.entity.Vibe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Portfolio {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vibe vibe;

    private String name;
    private String url;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

}
