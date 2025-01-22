package com.team4.toucheese.user.service;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.studio.entity.Reservation;
import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.ReservationRepository;
import com.team4.toucheese.studio.repository.StudioRepository;
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
            throw new IllegalArgumentException("reservation is null");
        }
        for (Reservation reservation : reservations){
            MyInfoDto dto = new MyInfoDto();
            if (reservation.getStatus().toString().equals("RESERVED")){
                dto.setStatus(reservation.getStatus().toString());
                dto.setStudioId(reservation.getStudio().getId());
                dto.setStudioName(reservation.getStudio().getName());
                dto.setMenuId(reservation.getMenu().getId());
                dto.setMenuName(reservation.getMenu().getName());
                dto.setDate(reservation.getDate());
                dto.setMenuImgUrl(reservation.getMenu().getMenuImages().get(0).getUrl());
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
