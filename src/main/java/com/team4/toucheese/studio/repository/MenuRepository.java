package com.team4.toucheese.studio.repository;

import com.team4.toucheese.studio.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStudioId(long studioId);
}
