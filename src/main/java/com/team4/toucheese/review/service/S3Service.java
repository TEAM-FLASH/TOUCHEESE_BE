package com.team4.toucheese.review.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.review.dto.CreateReviewRequestDto;
import com.team4.toucheese.review.dto.ReviewDto;
import com.team4.toucheese.review.entity.Review;
import com.team4.toucheese.review.entity.ReviewImage;
import com.team4.toucheese.review.repository.ReviewImageRepository;
import com.team4.toucheese.review.repository.ReviewRepository;
import com.team4.toucheese.studio.entity.AdditionalOption;
import com.team4.toucheese.studio.entity.Menu;
import com.team4.toucheese.studio.entity.Reservation;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.AdditionalOptionRepository;
import com.team4.toucheese.studio.repository.MenuRepository;
import com.team4.toucheese.studio.repository.ReservationRepository;
import com.team4.toucheese.studio.repository.StudioRepository;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final StudioRepository studioRepository;
    private final AdditionalOptionRepository additionalOptionRepository;
    private final ReservationRepository reservationRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    //허용된 확장자
    private final List<String> allowedFileExtensions = List.of(".jpg", ".jpeg", ".png", ".gif");
    // 허용된 이미지 MIME 타입 리스트
    private final List<String> allowedImageTypes = List.of("image/jpeg", "image/png", "image/gif");

    @Transactional
    public ReviewDto uploadReview(CreateReviewRequestDto createReviewRequestDto, Authentication authentication) {
        UserEntity user = getUser(authentication);
        Menu menu = getMenu(createReviewRequestDto.getMenuId());
        Studio studio = getStudio(menu.getStudio().getId());
        List<AdditionalOption> additionalOptions = new ArrayList<>();
        if (createReviewRequestDto.getAdditionalOptionIds() != null && !createReviewRequestDto.getAdditionalOptionIds().isEmpty()) {
            additionalOptions = getAdditionalOption(createReviewRequestDto.getAdditionalOptionIds());
        }

        // Create Review
        Review review = createReview(createReviewRequestDto, user, menu, studio, additionalOptions);

        // Upload files to S3
        if (createReviewRequestDto.getMultipartFiles() != null && !createReviewRequestDto.getMultipartFiles().isEmpty()) {
            List<String> fileUrlList = uploadFilesToS3(createReviewRequestDto.getMultipartFiles(), review);
        }

        Reservation reservation = reservationRepository.findById(createReviewRequestDto.getReservationId()).get();
        reservation.toBuilder().existReview(true).build();
        reservationRepository.save(reservation);

        // Save review
        reviewRepository.save(review);

        return ReviewDto.fromEntity(review);
    }

    private UserEntity getUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));
    }
    private Studio getStudio(Long studioId){
        return studioRepository.findById(studioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스튜디오를 찾을 수 없습니다."));
    }
    private List<AdditionalOption> getAdditionalOption(List<Long> additionalOptionIds){
        List<AdditionalOption> additionalOptions = new ArrayList<>();
        for (Long additionalOptionId : additionalOptionIds) {
            AdditionalOption additionalOption = additionalOptionRepository.findById(additionalOptionId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "추가옵션을 찾을 수 없습니다"));
            additionalOptions.add(additionalOption);
        }
        return additionalOptions;
    }

    private Review createReview(CreateReviewRequestDto requestDto, UserEntity user, Menu menu, Studio studio, List<AdditionalOption> additionalOptions) {
        return Review.builder()
                .content(requestDto.getContent())
                .menu(menu)
                .user(user)
                .studio(studio)
                .additionalOptions(additionalOptions)
                .rating(requestDto.getRating())
                .reservationId(requestDto.getReservationId())
                .build();
    }

    private List<String> uploadFilesToS3(List<MultipartFile> files, Review review) {
        List<String> fileUrlList = new ArrayList<>();
        List<ReviewImage> reviewImages = new ArrayList<>();

        files.forEach(file -> {
            validateImageFile(file);

            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                String fileUrl = amazonS3.getUrl(bucket, fileName).toString();
                fileUrlList.add(fileUrl);

                ReviewImage reviewImage = ReviewImage.builder()
                        .review(review)
                        .url(fileUrl)
                        .build();

                // Review에 추가하여 양방향 매핑 유지
                review.getReviewImages().add(reviewImage);

                reviewImages.add(reviewImage);
            } catch (IOException | AmazonClientException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패", e);
            }
        });

        return fileUrlList;
    }

    // validateImageFile 메서드에 확장자 확인 추가:
    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        String fileExtension = getFileExtension(file.getOriginalFilename());

        if (contentType == null || !allowedImageTypes.contains(contentType) || !allowedFileExtensions.contains(fileExtension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "지원되지 않는 파일 형식입니다: " + contentType + " (" + fileExtension + ")");
        }
    }

    //파일명을 난수화하기 위해 UUID를 활용하여 난수를 돌린다
    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // "."의 존재 유무만 판단
    private String getFileExtension(String fileName) {
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println(bucket);
    }
}

