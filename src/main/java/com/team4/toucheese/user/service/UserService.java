package com.team4.toucheese.user.service;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.studio.entity.CancelReservation;
import com.team4.toucheese.studio.entity.CompleteReservation;
import com.team4.toucheese.studio.entity.Reservation;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.CancelReservationRepository;
import com.team4.toucheese.studio.repository.CompleteReservationRepository;
import com.team4.toucheese.studio.repository.ReservationRepository;
import com.team4.toucheese.studio.repository.StudioRepository;
import com.team4.toucheese.user.dto.*;
import com.team4.toucheese.user.entity.BookMark;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.BookMarkRepository;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Transactional
    public void addBookMark(Long userId, Long studioId){
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
    }

    @Transactional
    public void deleteBookMark(Long userId, Long studioId){
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserEntity> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        UserEntity userEntity = user.get().toBuilder().phone(phone).build();
        userRepository.save(userEntity);

        ChangePhoneResultDTO changePhoneResultDTO = new ChangePhoneResultDTO();
        changePhoneResultDTO.setMessage("Change Phone Success");
        return changePhoneResultDTO;
    }

    //비밀번호 변경
    @Transactional
    public ChangePasswordResultDTO changePassword(Authentication authentication, String password){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserEntity> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()){
            throw new IllegalArgumentException("user is null");
        }
        UserEntity userEntity = user.get().toBuilder().password(password).build();
        userRepository.save(userEntity);

        ChangePasswordResultDTO changePasswordResultDTO = new ChangePasswordResultDTO();
        changePasswordResultDTO.setMessage("Change Password Success");
        return changePasswordResultDTO;
    }

}
