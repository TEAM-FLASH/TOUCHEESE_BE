package com.team4.toucheese.user.service;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.studio.dto.StudioDto;
import com.team4.toucheese.studio.entity.CancelReservation;
import com.team4.toucheese.studio.entity.CompleteReservation;
import com.team4.toucheese.studio.entity.Reservation;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.*;
import com.team4.toucheese.user.dto.*;
import com.team4.toucheese.user.entity.BookMark;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.BookMarkRepository;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;
    private final StudioRepository studioRepository;
    private final ReservationRepository reservationRepository;
    private final CompleteReservationRepository completeReservationRepository;
    private final CancelReservationRepository cancelReservationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdditionalOptionRepository additionalOptionRepository;

    @Transactional
    public BookmarkResultDto addBookMark(Authentication authentication, Long studioId){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = customUserDetails.getEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        Long userId = user.getId();
        BookMark bookMark = BookMark.builder()
                .userId(userId)
                .studioId(studioId)
                .build();
        Studio studio = studioRepository.findById(studioId).orElse(null);
        if (studio != null){
            // 북마크 추가가 안되어 있을때
            if (!checkBookMark(userId, studioId)){
                //studio의 bookmark_count를 +1
                Studio updatedStudio = studio.toBuilder().bookmark_count(studio.getBookmark_count() + 1).build();

                bookMarkRepository.save(bookMark);
                studioRepository.save(updatedStudio);
            }
        }
        BookmarkResultDto bookmarkResultDto = new BookmarkResultDto();
        bookmarkResultDto.setType("add");
        bookmarkResultDto.setSuccess(true);
        bookmarkResultDto.setMessage("북마크 추가에 성공했습니다");
        return bookmarkResultDto;
    }

    @Transactional
    public BookmarkResultDto deleteBookMark(Authentication authentication, Long studioId){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = customUserDetails.getEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        Long userId = user.getId();
        BookMark bookMark = (BookMark) bookMarkRepository.findByUserIdAndStudioId(userId, studioId).orElse(null);
        if (bookMark != null){
            Studio studio = studioRepository.findById(studioId).orElse(null);
            Long currentCount = studio.getBookmark_count();
            Studio updatedStudio = studio.toBuilder()
                    .bookmark_count(currentCount > 0 ? currentCount-1 : 0)
                    .build();
            studioRepository.save(updatedStudio);
            bookMarkRepository.delete(bookMark);
        }
        BookmarkResultDto bookmarkResultDto = new BookmarkResultDto();
        bookmarkResultDto.setType("delete");
        bookmarkResultDto.setSuccess(true);
        bookmarkResultDto.setMessage("북마크 삭제에 성공했습니다");

        return bookmarkResultDto;
    }

    public boolean checkBookMark(Long userId, Long studioId){
        return bookMarkRepository.findByUserIdAndStudioId(userId, studioId).isPresent();
    }

    public List<MyInfoDto> getMyInfo(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();
//        System.out.println("email = " + email);
        if (email == null){
            throw new IllegalArgumentException("email is null");
        }
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        Long userId = user.get().getId();
        List<Reservation> reservations = reservationRepository.findByUser(userId);
        List<MyInfoDto> dtos = new ArrayList<>();
        if (reservations.isEmpty()){
            return null;
        }
        for (Reservation reservation : reservations){
            MyInfoDto dto = new MyInfoDto();
            if (reservation.getStatus().toString().equals("RESERVED")){
                dto.setReservationId(reservation.getId());
                dto.setStatus(reservation.getStatus().toString());
                dto.setStudioId(reservation.getStudio().getId());
                dto.setStudioName(reservation.getStudio().getName());
                dto.setMenuId(reservation.getMenu().getId());
                dto.setMenuName(reservation.getMenu().getName());
                dto.setDate(reservation.getDate());
                dto.setStartTime(reservation.getStart_time());
                dto.setMenuImgUrl(reservation.getMenu().getMenuImages().get(0).getUrl());
                if (!reservation.getAdditionalOptionIds().isEmpty()){
                    dto.setAdditionalOptionIds(reservation.getAdditionalOptionIds());
                    List<String> additionalOptionNames = new ArrayList<>();
                    for (Long additionalOptionId : reservation.getAdditionalOptionIds()){
                        String additionalOptionName = additionalOptionRepository.findById(additionalOptionId).get().getName();
                        additionalOptionNames.add(additionalOptionName);
                    }
                    dto.setAdditionalOptionNames(additionalOptionNames);
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    //이용예정 목록
    public List<MyInfoDto> getMyReservation(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();
//        System.out.println("email = " + email);
        if (email == null){
            throw new IllegalArgumentException("email is null");
        }
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        Long userId = user.get().getId();
        List<Reservation> reservations = reservationRepository.findByUser(userId);
        List<MyInfoDto> dtos = new ArrayList<>();
        if (reservations.isEmpty()){
            return null;
        }
        for (Reservation reservation : reservations){
            MyInfoDto dto = new MyInfoDto();
            if (reservation.getStatus().toString().equals("RESERVED") || reservation.getStatus().toString().equals("WAITING")){
                dto.setReservationId(reservation.getId());
                dto.setStatus(reservation.getStatus().toString());
                dto.setStudioId(reservation.getStudio().getId());
                dto.setStudioName(reservation.getStudio().getName());
                dto.setMenuId(reservation.getMenu().getId());
                dto.setMenuName(reservation.getMenu().getName());
                dto.setDate(reservation.getDate());
                dto.setStartTime(reservation.getStart_time());
                dto.setMenuImgUrl(reservation.getMenu().getMenuImages().get(0).getUrl());
                if (!reservation.getAdditionalOptionIds().isEmpty()){
                    dto.setAdditionalOptionIds(reservation.getAdditionalOptionIds());
                    List<String> additionalOptionNames = new ArrayList<>();
                    for (Long additionalOptionId : reservation.getAdditionalOptionIds()){
                        String additionalOptionName = additionalOptionRepository.findById(additionalOptionId).get().getName();
                        additionalOptionNames.add(additionalOptionName);
                    }
                    dto.setAdditionalOptionNames(additionalOptionNames);
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    //이용완료 목록
    public List<MyInfoDto> getMyComplete(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();

        if (email == null){
            throw new IllegalArgumentException("email is null");
        }
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        Long userId = user.get().getId();
        List<Reservation> reservations = reservationRepository.findByUser(userId);
        List<MyInfoDto> dtos = new ArrayList<>();
        if (reservations.isEmpty()){
            return null;
        }
        for (Reservation reservation : reservations){
            MyInfoDto dto = new MyInfoDto();
            if (reservation.getStatus().toString().equals("COMPLETED")){
                dto.setReservationId(reservation.getId());
                dto.setStatus(reservation.getStatus().toString());
                dto.setStudioId(reservation.getStudio().getId());
                dto.setStudioName(reservation.getStudio().getName());
                dto.setMenuId(reservation.getMenu().getId());
                dto.setMenuName(reservation.getMenu().getName());
                dto.setDate(reservation.getDate());
                dto.setStartTime(reservation.getStart_time());
                dto.setMenuImgUrl(reservation.getMenu().getMenuImages().get(0).getUrl());
                if (!reservation.getAdditionalOptionIds().isEmpty()){
                    dto.setAdditionalOptionIds(reservation.getAdditionalOptionIds());
                    List<String> additionalOptionNames = new ArrayList<>();
                    for (Long additionalOptionId : reservation.getAdditionalOptionIds()){
                        String additionalOptionName = additionalOptionRepository.findById(additionalOptionId).get().getName();
                        additionalOptionNames.add(additionalOptionName);
                    }
                    dto.setAdditionalOptionNames(additionalOptionNames);
                }
                dtos.add(dto);
            }

        }
        return dtos;
    }

    //예약취소 목록
    public List<MyInfoDto> getMyCancel(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();

        if (email == null){
            throw new IllegalArgumentException("email is null");
        }
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        Long userId = user.get().getId();
        List<Reservation> reservations = reservationRepository.findByUser(userId);
        List<MyInfoDto> dtos = new ArrayList<>();
        if (reservations.isEmpty()){
            return null;
        }
        for (Reservation reservation : reservations){
            MyInfoDto dto = new MyInfoDto();
            if (reservation.getStatus().toString().equals("CANCELED")){
                dto.setReservationId(reservation.getId());
                dto.setStatus(reservation.getStatus().toString());
                dto.setStudioId(reservation.getStudio().getId());
                dto.setStudioName(reservation.getStudio().getName());
                dto.setMenuId(reservation.getMenu().getId());
                dto.setMenuName(reservation.getMenu().getName());
                dto.setDate(reservation.getDate());
                dto.setStartTime(reservation.getStart_time());
                if (!reservation.getAdditionalOptionIds().isEmpty()){
                    dto.setAdditionalOptionIds(reservation.getAdditionalOptionIds());
                    List<String> additionalOptionNames = new ArrayList<>();
                    for (Long additionalOptionId : reservation.getAdditionalOptionIds()){
                        String additionalOptionName = additionalOptionRepository.findById(additionalOptionId).get().getName();
                        additionalOptionNames.add(additionalOptionName);
                    }
                    dto.setAdditionalOptionNames(additionalOptionNames);
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    //예약 상세

    //예약 취소 신청


    //핸드폰 번호 변경
    @Transactional
    public ChangePhoneResultDTO changePhone(Authentication authentication, String phone){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Optional<UserEntity> user = userRepository.findByEmail(userDetails.getEmail());
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        UserEntity userEntity = user.get().toBuilder().phone(phone).build();
        userRepository.save(userEntity);

        ChangePhoneResultDTO changePhoneResultDTO = new ChangePhoneResultDTO();
        changePhoneResultDTO.setMessage("Change Phone Success");
        changePhoneResultDTO.setSuccess(Boolean.TRUE);
        return changePhoneResultDTO;
    }

    //비밀번호 변경
    @Transactional
    public ChangePasswordResultDTO changePassword(Authentication authentication, String password){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        System.out.println("password = " + password);
//        System.out.println(userDetails.getEmail());
        Optional<UserEntity> user = userRepository.findByEmail(userDetails.getEmail());
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        UserEntity userEntity = user.get().toBuilder().password(passwordEncoder.encode(password)).build();
        userRepository.save(userEntity);

        ChangePasswordResultDTO changePasswordResultDTO = new ChangePasswordResultDTO();
        changePasswordResultDTO.setMessage("Change Password Success");
        changePasswordResultDTO.setSuccess(Boolean.TRUE);
        return changePasswordResultDTO;
    }

    public List<StudioDto> getMyBookmark(Authentication authentication) {
        // 인증된 유저 정보 확인
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 이메일로 사용자 조회
        UserEntity user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // 북마크한 스튜디오 목록 조회
        List<BookMark> bookMarks = bookMarkRepository.findAllByUserId(user.getId());

        // 북마크가 없을 경우, 빈 리스트 반환
        if (bookMarks.isEmpty()) {
            return new ArrayList<>();
        }

        // 북마크에서 스튜디오 ID 추출 (stream 사용)
        List<Long> studioIds = bookMarks.stream()
                .map(BookMark::getStudioId)
                .toList();

        // 스튜디오 ID 리스트로 스튜디오 데이터 한번에 조회
        List<Studio> studios = studioRepository.findAllById(studioIds);

        // Studio -> StudioDto 변환
        return studios.stream()
                .map(studio -> {
                    StudioDto dto = StudioDto.fromEntity(studio);
                    dto.setBookmark(true);
                    return dto;
                })
                .toList();
    }

}
