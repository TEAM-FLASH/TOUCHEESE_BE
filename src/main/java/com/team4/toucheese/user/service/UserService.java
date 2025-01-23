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
import com.team4.toucheese.user.dto.MyCanceledInfo;
import com.team4.toucheese.user.dto.MyCompletedInfo;
import com.team4.toucheese.user.dto.MyInfoDto;
import com.team4.toucheese.user.entity.BookMark;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.BookMarkRepository;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
        return dtos;
    }

    //이용완료 목록
    public List<MyCompletedInfo> getMyComplete(Authentication authentication){
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
        List<CompleteReservation> completeReservations = completeReservationRepository.findByUser(userId);
        List<MyCompletedInfo> dtos = new ArrayList<>();
        if (completeReservations.isEmpty()){
            return null;
        }
        for (CompleteReservation completeReservation : completeReservations){
            MyCompletedInfo dto = new MyCompletedInfo();
            dto.setCompletedReservationId(completeReservation.getId());
            dto.setStudioId(completeReservation.getStudio().getId());
            dto.setStudioName(completeReservation.getStudio().getName());
            dto.setMenuId(completeReservation.getMenu().getId());
            dto.setMenuName(completeReservation.getMenu().getName());
            dto.setDate(completeReservation.getDate());
            dto.setStartTime(completeReservation.getStart_time());
            dto.setMenuImgUrl(completeReservation.getMenu().getMenuImages().get(0).getUrl());
            dto.setStatus(completeReservation.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }

    //예약취소 목록
    public List<MyCanceledInfo> getMyCancel(Authentication authentication){
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
        List<CancelReservation> cancelReservations = cancelReservationRepository.findByUser(userId);
        List<MyCanceledInfo> dtos = new ArrayList<>();
        if (cancelReservations.isEmpty()){
            return null;
        }
        for (CancelReservation cancelReservation : cancelReservations){
            MyCanceledInfo dto = new MyCanceledInfo();
            dto.setCompletedReservationId(cancelReservation.getId());
            dto.setStudioId(cancelReservation.getStudio().getId());
            dto.setStudioName(cancelReservation.getStudio().getName());
            dto.setMenuId(cancelReservation.getMenu().getId());
            dto.setMenuName(cancelReservation.getMenu().getName());
            dto.setDate(cancelReservation.getDate());
            dto.setStartTime(cancelReservation.getStart_time());
            dto.setMenuImgUrl(cancelReservation.getMenu().getMenuImages().get(0).getUrl());
            dto.setStatus(cancelReservation.getStatus());
            dto.setCancelReason(cancelReservation.getCancelReason());
            dto.setCancelReasonDetail(cancelReservation.getCancelReasonDetail());
            dtos.add(dto);
        }
        return dtos;
    }

    //예약 상세

    //예약 취소 신청


    //핸드폰 번호 변경

    //비밀번호 변경
}
