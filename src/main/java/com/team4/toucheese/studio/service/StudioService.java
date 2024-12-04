package com.team4.toucheese.studio.service;

import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.entity.StudioOption;
import com.team4.toucheese.studio.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final String SEARCH_KEY = "real_time_keyword";
    private final String TIMESTAMP_KEY = "keyword_timestamp";
    private final long EXPIRE_TIME = 60 * 60;   //1시간 (60초 * 60)
    private final RedisTemplate<String, String> redisTemplate;

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
            int maxPrice,
            List<String> options
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
        if (options != null) {
            for (String option : options) {
                if (option.equals("HAIR_MAKEUP")) {
                    try{
                        StudioOption.OptionName optionName = StudioOption.OptionName.valueOf(option);
                        studios = studios.stream()
                                .filter(studio -> studioRepository.findByOptionsExists(optionName).contains(studio))
                                .collect(Collectors.toList());
                    }catch (IllegalArgumentException e){
                        System.err.println(e + option);
                    }
                }
                if (option.equals("CHANGING_ROOM")) {
                    try{
                        StudioOption.OptionName optionName = StudioOption.OptionName.valueOf(option);
                        studios = studios.stream()
                                .filter(studio -> studioRepository.findByOptionsExists(optionName).contains(studio))
                                .collect(Collectors.toList());
                    }catch (IllegalArgumentException e){
                        System.err.println(e + option);
                    }
                }
                if (option.equals("DRESSING_ROOM")) {
                    try{
                        StudioOption.OptionName optionName = StudioOption.OptionName.valueOf(option);
                        studios = studios.stream()
                                .filter(studio -> studioRepository.findByOptionsExists(optionName).contains(studio))
                                .collect(Collectors.toList());
                    }catch (IllegalArgumentException e){
                        System.err.println(e + option);
                    }
                }
                if (option.equals("SUIT_RENTAL_FREE")) {
                    try{
                        StudioOption.OptionName optionName = StudioOption.OptionName.valueOf(option);
                        studios = studios.stream()
                                .filter(studio -> studioRepository.findByOptionsExists(optionName).contains(studio))
                                .collect(Collectors.toList());
                    }catch (IllegalArgumentException e){
                        System.err.println(e + option);
                    }
                }
                if (option.equals("ORIGINAL_FILES")) {
                    try{
                        StudioOption.OptionName optionName = StudioOption.OptionName.valueOf(option);
                        studios = studios.stream()
                                .filter(studio -> studioRepository.findByOptionsExists(optionName).contains(studio))
                                .collect(Collectors.toList());
                    }catch (IllegalArgumentException e){
                        System.err.println(e + option);
                    }
                }
                if (option.equals("INDIVIDUAL_EDITING")) {
                    try{
                        StudioOption.OptionName optionName = StudioOption.OptionName.valueOf(option);
                        studios = studios.stream()
                                .filter(studio -> studioRepository.findByOptionsExists(optionName).contains(studio))
                                .collect(Collectors.toList());
                    }catch (IllegalArgumentException e){
                        System.err.println(e + option);
                    }
                }
            }
        }


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
    public Page<StudioDto> getStudiosWithSearch(String keyword, Pageable pageable){

        //검색어가 포함되어 있는 스튜디오
        List<Studio> studios = studioRepository.findByNameContaining(keyword);

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

    //Redis 검색어 저장
    public void saveKeyword(String keyword){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        long currentTime = System.currentTimeMillis() / 1000;   //현재시간 (초)

        //검색 횟수 증가
        zSetOps.incrementScore(SEARCH_KEY, keyword, 1);

        //검색어의 최신 타임스탬프 저장
        redisTemplate.opsForHash().put(TIMESTAMP_KEY, keyword, String.valueOf(currentTime));

    }

    //실시간 검색어
    public Set<String> getTopKeyword(int topN){
        //오래된 검색어 제거
        clearOldKeyword();

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(SEARCH_KEY, 0, topN - 1);   //검색 횟수 기준 상위 N개 반환
    }

    //오래된 검색어 정리
    public void clearOldKeyword(){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        long currentTime = System.currentTimeMillis() / 1000;

        Set<String> keywords = zSetOps.range(SEARCH_KEY, 0, -1);    //모든 검색어
        if (keywords != null) {
            for (String keyword : keywords) {
                String timestampStr = (String) redisTemplate.opsForHash().get(TIMESTAMP_KEY, keyword);
                if (timestampStr != null){
                    long timestamp = Long.parseLong(timestampStr);
                    if (currentTime - timestamp > EXPIRE_TIME){
                        zSetOps.remove(SEARCH_KEY, keyword);
                    }
                }
            }
        }
    }



}
