package com.team4.toucheese.studio.repository;

import com.team4.toucheese.address.entity.Address_Gu;
import com.team4.toucheese.studio.entity.Portfolio;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.entity.StudioOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    Page<Studio> findAll(Pageable pageable);

    //예약간의 중첩 여부 확인
    @Query("SELECT s FROM Studio s " +
            "WHERE s.day_of_week <> :dayOfWeek " + // 1. 휴무일이 아님
            "AND NOT EXISTS (" +
            "    SELECT r FROM Reservation r " +
            "    WHERE r.studio = s " +
            "    AND r.date = :date " +
            "    AND r.start_time < :endTime " +
            "    AND r.end_time > :startTime" +
            ") " + // 2. 예약이 겹치지 않음
            "AND NOT EXISTS (" +
            "    SELECT sc FROM Schedule sc " +
            "    WHERE sc.studio = s " +
            "    AND sc.date = :date" +
            ") " + // 3. 스케줄에 쉬는 날이 아님
            "AND s.open_time <= :startTime " + // 4. 오픈 시간 이내
            "AND s.close_time > :endTime")   // 마감 시간 이내
    List<Studio> findAvailableStudios(@Param("date") LocalDate date,
                                      @Param("startTime") LocalTime startTime,
                                      @Param("endTime") LocalTime endTime,
                                      @Param("dayOfWeek") Studio.DayOfWeek dayOfWeek);

    //분위기에 따라 스튜디오, 포폴 필터링
    @Query("SELECT s FROM Studio s " +
            "LEFT JOIN s.portfolios p " + // 스튜디오와 연관된 포트폴리오를 가져옴
            "WHERE (s.vibe.name = :vibeName OR s.subVibe.name = :vibeName) "// 스튜디오 분위기 조건
//           + "AND (p IS NULL OR p.vibe.name = :vibeName)" // 포트폴리오가 없거나, 있으면 필터링 조건 만족함
           )
    List<Studio> findStudiosAndFilteredPortfolios(@Param("vibeName") String vibeName);

    //지역에 따라 스튜디오 필터링
    List<Studio> findByAddressGu_Name(String addressGu);

    //가격에 따라 스튜디오 필터링
    @Query("SELECT s FROM Studio s " +
            "LEFT JOIN s.menus m " +
            "WHERE m.name = '프로필사진' AND m.price BETWEEN :minPrice AND :maxPrice")
    List<Studio> findByPrice(@Param("minPrice") int minPrice,
                             @Param("maxPrice") int maxPrice);

    //옵션에 따라 스튜디오 필터링
//    List<Studio> findByOptionsExists(List<String> options);

    //검색어가 상호명에 포함된 스튜디오 찾기
    List<Studio> findByNameContaining(String str);






}


