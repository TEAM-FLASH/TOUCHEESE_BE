package com.team4.toucheese.studio.service;

import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;

    //모든 스튜디오 정보 가져와서 페이징
    public Page<StudioDto> getAllStudios(int page, int size, SortBy sortby){
        //데이터 베이스에서 모든 데이터 가져옴
        List<Studio> studios = studioRepository.findAll();

        //데이터 DTO로 반환
        List<StudioDto> studioDtos = studios.stream()
                .map(StudioDto::fromEntity)
                .collect(Collectors.toList());

        //정렬
        studioDtos = sortStudios(studioDtos, sortby);

        //페이징 적용
        // 페이지 요청에 따른 페이징을 적용합니다.
        int start = page * size;
        int end = Math.min(start + size, studioDtos.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, size), studioDtos.size());
        }

        List<StudioDto> pagedAndSortedStudios = studioDtos.subList(start, end);

        return new PageImpl<>(pagedAndSortedStudios, PageRequest.of(page, size), studioDtos.size());
    }

    //필터링으로 스튜디오 보여주기
    public Page<StudioDto> getStudiosWithFilter(
            LocalDateTime requestedDateTime,
            LocalTime duration,
            String vibeName,
            String addressGu,
            Pageable pageable,
            SortBy sortBy,
            int minPrice,
            int maxPrice
    ) {
        // 스튜디오 담을 리스트
        List<Studio> studios;

        // 요청된 날짜와 시간으로 startTime과 endTime을 설정
        if (requestedDateTime != null && duration != null) {
            LocalDate date = requestedDateTime.toLocalDate();
            LocalTime startTime = requestedDateTime.toLocalTime();
            LocalTime endTime = startTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute());
            Studio.DayOfWeek dayOfWeek = Studio.DayOfWeek.valueOf(requestedDateTime
                    .getDayOfWeek()
                    .toString()
                    .substring(0, 3)
                    .toUpperCase());
            studios = studioRepository.findAvailableStudios(date, startTime, endTime, dayOfWeek);
        }else {
            studios = studioRepository.findAll();
        }

        // vibeName 필터링
        if (vibeName != null) {
            studios = studios.stream()
                    .filter(studio -> studioRepository.findStudiosAndFilteredPortfolios(vibeName).contains(studio))
                    .collect(Collectors.toList());
        }

        // addressGu 필터링
        if (addressGu != null) {
            studios = studios.stream()
                    .filter(studio -> studioRepository.findByAddressGu_Name(addressGu).contains(studio))
                    .collect(Collectors.toList());
        }

        // 가격 필터링
        if (minPrice != -1 && maxPrice != -1){
            studios = studios.stream()
                    .filter(studio -> studioRepository.findByPrice(minPrice, maxPrice).contains(studio))
                    .collect(Collectors.toList());
        }
        // 옵션 필터링


        //정렬 적용
        List<StudioDto> studioDtos = studios.stream()
                .map(StudioDto::fromEntity)
                .collect(Collectors.toList());

        studioDtos = sortStudios(studioDtos, sortBy);

        //페이징 적용

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), studioDtos.size());


        return new PageImpl<>(studioDtos.subList(start, end), pageable, studioDtos.size());

    }

    //검색한 스튜디오
    public Page<StudioDto> getStudiosWithSearch(String str, Pageable pageable){

        //검색어가 포함되어 있는 스튜디오
        List<Studio> studios = studioRepository.findByNameContaining(str);

        //DTO로 반환
        List<StudioDto> studioDtos = studios.stream()
                .map(StudioDto::fromEntity)
                .collect(Collectors.toList());

        //페이징
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), studioDtos.size());

        return new PageImpl<>(studioDtos.subList(start, end), pageable, studioDtos.size());
    }

    //정렬에 관련
    //ENUM
    public enum SortBy {
        POPULARITY,
        VIEW_COUNT,
        RATING,
        REVIEW_COUNT
    }
    private List<StudioDto> sortStudios(
            List<StudioDto> studios,
            SortBy sortBy
    ){
        switch (sortBy){
            case POPULARITY:
                studios.sort(Comparator.comparing
                        (StudioDto::getBookmark_count).reversed());
                break;
            case VIEW_COUNT:
                    studios.sort(Comparator.comparing
                            (StudioDto::getView_count).reversed());
                break;
            case RATING:
                studios.sort(Comparator.comparing
                        (StudioDto::getRating).reversed());
                break;
            case REVIEW_COUNT:
                studios.sort(Comparator.comparing
                        (StudioDto::getReview_count).reversed());
                break;
            default:
                studios.sort(Comparator.comparing
                        (StudioDto::getName).reversed());

        }
        return studios;
    }
}
