package com.team4.toucheese.review.service;

import com.team4.toucheese.review.dto.ReviewDetailWithTotal;
import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.entity.ReviewImage;
import com.team4.toucheese.review.repository.ReviewImageRepository;
import com.team4.toucheese.review.repository.ReviewRepository;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageService reviewImageService;
    private final MenuRepository menuRepository;
    private final ReviewImageRepository reviewImageRepository;

    public List<ReviewDto> findStudioReview(Long studioId){
        if (studioId == null){
            throw new IllegalArgumentException("studioId is null");
        }

        //스튜디오의 리뷰 찾기
        List<Review> reviews = reviewRepository.findByMenu_Studio_Id(studioId);

        if (reviews.isEmpty()){
            return List.of();
        }
        //데이터 DTO로 변환
        List<ReviewDto> reviewDtos = reviews.stream().map(review -> {
            ReviewDto reviewDto = ReviewDto.fromEntity(review);

            //이미지가 존재하는지
            if (reviewImageService.existReviewImage(review.getId())){
                reviewDto.setImageExists(true);
            }
            return reviewDto;
        }).toList();

        return reviewDtos;
    }

    public ReviewDetailWithTotal findReviewWithTotal(Long studioId, Pageable pageable, long menuId){
        ReviewDetailWithTotal reviewDetailWithTotal = new ReviewDetailWithTotal();

        //전체 스튜디오
        List<ReviewDto> reviewDtos = findStudioReview(studioId);
        int totalSize = reviewDtos.size();

        int totalRating = 0;
        int totalImageNum = 0;
        for (ReviewDto reviewDto : reviewDtos){
            totalRating += reviewDto.getRating();
            totalImageNum += reviewDto.getReviewImages().size();
        }
        double avgRating = totalSize > 0 ? (double) totalRating / totalSize : 0;

        //메뉴 Id에 따른 필터링
        List<ReviewDto> filteredReviews;
        if ( menuId != -1 ) {
            filteredReviews = reviewDtos.stream().filter(reviewDto -> reviewDto.getMenuId() == menuId).toList();
        }else {
            filteredReviews = reviewDtos;
        }

        //페이징
        //시작
        int start = (int) pageable.getOffset();
        //끝
        int end = Math.min((start + pageable.getPageSize()), filteredReviews.size());
        List<ReviewDto> pagedReviews;
        if (start > filteredReviews.size()){
            //시작값이 리스트 크기를 초과한 경우
            pagedReviews = List.of();
        }else {
            //정상적인 경우
            pagedReviews = filteredReviews.subList(start, end);
        }

        //메뉴 이름 찾기
        List<Menu> menus = menuRepository.findByStudioId(studioId);

        //리뷰 샘플 사진
        List<ReviewImage> reviewImages = reviewImageService.findByStudio(studioId);
        List<String> reviewImageUrls = reviewImages.stream().map(ReviewImage::getUrl).toList();


        reviewDetailWithTotal.setMenuNanmeList(menus.stream().map(Menu::getName).toList());
        reviewDetailWithTotal.setMenuIdList(menus.stream().map(Menu::getId).toList());
        reviewDetailWithTotal.setSamplePhotoList(reviewImageUrls);
        reviewDetailWithTotal.setReviewList(pagedReviews);
        reviewDetailWithTotal.setTotalImageNum(totalImageNum);
        reviewDetailWithTotal.setAvgRating(avgRating);
        reviewDetailWithTotal.setTotalReviewNum(totalSize);


        return reviewDetailWithTotal;

    }

    public Integer countReviewNum(Long menuId){
        return reviewRepository.findByMenu_Id(menuId).size();
    }

}
