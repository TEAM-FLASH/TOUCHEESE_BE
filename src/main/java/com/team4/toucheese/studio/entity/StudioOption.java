package com.team4.toucheese.studio.entity;

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
public class StudioOption {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private Studio studio;

    @Enumerated(EnumType.STRING)
    private OptionName name;
    private String description;

    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

    public enum OptionName {
        //6가지 옵션
        HAIR_MAKEUP,
        CHANGING_ROOM,
        DRESSING_ROOM,
        SUIT_RENTAL_FREE,
        ORIGINAL_FILES,
        INDIVIDUAL_EDITING
    }
}
