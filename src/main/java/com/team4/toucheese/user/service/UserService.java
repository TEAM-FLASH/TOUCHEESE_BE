package com.team4.toucheese.user.service;

import com.team4.toucheese.studio.entity.Studio;
import com.team4.toucheese.studio.repository.StudioRepository;
import com.team4.toucheese.user.entity.BookMark;
import com.team4.toucheese.user.repository.BookMarkRepository;
import com.team4.toucheese.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;
    private final StudioRepository studioRepository;

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
                Long bookMarkCount = studio.getBookmark_count();
                bookMarkCount++;
                studio.toBuilder().bookmark_count(bookMarkCount).build();

                bookMarkRepository.save(bookMark);
                studioRepository.save(studio);
            }
        }
    }

    @Transactional
    public void deleteBookMark(Long userId, Long studioId){
        BookMark bookMark = (BookMark) bookMarkRepository.findByUserIdAndStudioId(userId, studioId).orElse(null);
        if (bookMark != null){
            Studio studio = studioRepository.findById(studioId).orElse(null);
            Long bookMarkCount = studio.getBookmark_count();
            if (bookMarkCount <= 0){
                bookMarkCount = 0L;
                studio.toBuilder().bookmark_count(bookMarkCount).build();
            }else {
                studio.toBuilder().bookmark_count(bookMarkCount - 1).build();
            };
            studioRepository.save(studio);
            bookMarkRepository.delete(bookMark);
        }
    }

    public boolean checkBookMark(Long userId, Long studioId){
        return bookMarkRepository.findByUserIdAndStudioId(userId, studioId).isPresent();
    }
}
