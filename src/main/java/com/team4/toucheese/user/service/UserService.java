package com.team4.toucheese.user.service;

import com.team4.toucheese.user.entity.BookMark;
import com.team4.toucheese.user.repository.BookMarkRepository;
import com.team4.toucheese.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    public void addBookMark(Long userId, Long studioId){
        BookMark bookMark = BookMark.builder()
                .userId(userId)
                .studioId(studioId)
                .build();
        bookMarkRepository.save(bookMark);
    }

    public void deleteBookMark(Long userId, Long studioId){
        BookMark bookMark = (BookMark) bookMarkRepository.findByIdAndStudioId(userId, studioId).orElse(null);
        if (bookMark != null){
            bookMarkRepository.delete(bookMark);
        }
    }
}
