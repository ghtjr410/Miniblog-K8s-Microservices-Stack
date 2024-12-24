package com.miniblog.query.controller.user;

import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Post;
import com.miniblog.query.model.Profile;
import com.miniblog.query.service.user.UserQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/query-service")
@RequiredArgsConstructor
@Slf4j
public class UserQueryController {
    private final UserQueryService userService;

    // 1. 특정유저가 좋아요를 누른 게시글 목록
    @GetMapping("/user/{userUuid}/liked-posts")
    public Page<Post> getPostsLikedByUser(
            @PathVariable String userUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsLikedByUser(userUuid, page, size);
    }
    // 2. 특정유저가 댓글을 작성한 게시글 목록
    @GetMapping("/user/{userUuid}/commented-posts")
    public Page<Post> getCommentsByUser(
            @PathVariable String userUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getCommentsByUser(userUuid, page, size);
    }
    //todo: 3. 사용자 닉네임을 기준으로 작성한 게시글 모든 목록 가져오기
    @GetMapping("/user/{nickname}/posts")
    public List<Post> getPostsByNickname(@PathVariable String nickname) {
        return userService.getPostsByNickname(nickname);
        // todo: 확장성을고려하여  return ResponseEntity.ok(posts); // 상태 코드 200 명시적 반환 이것도 생각해봐도 좋을듯
    }
    //todo: 4. 사용자 닉네임을 기준으로 작성한 게시글 최신순으로 20개씩 가져오기
    @GetMapping("/user/{nickname}/posts/latest")
    public Page<Post> getPostsByNicknameOrderByCreatedDateDesc(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsByNicknameOrderByCreatedDateDesc(nickname, page, size);
    }
    //todo:5. 사용자 닉네임을 기준으로 작성한 게시글 좋아요순으로 20개씩 가져오기
    @GetMapping("/user/{nickname}/posts/most-liked")
    public Page<Post> getPostsByNicknameOrderByLikeCountDesc(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsByNicknameOrderByLikeCountDesc(nickname, page, size);
    }
    //todo: 6. 사용자 닉네임을 기준으로 작성한 게시글 조회수순으로 20개씩 가져오기
    @GetMapping("/user/{nickname}/posts/most-viewed")
    public Page<Post> getPostsByNicknameOrderByTotalViewsDesc(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsByNicknameOrderByTotalViewsDesc(nickname, page, size);
    }
    //todo: 7. 사용자 닉네임을 기준으로 프로필 정보 가져오기
    @GetMapping("/user/{nickname}/profile")
    public ResponseEntity<Profile> getProfileByNickname(@PathVariable String nickname) {
        Optional<Profile> profile = userService.getProfileByNickname(nickname);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //todo: 8. 사용자 닉네임을 기준으로 제목으로 검색하기
    @GetMapping("/user/{nickname}/posts/search/title")
    public Page<Post> searchPostsByNicknameAndTitle(
            @PathVariable String nickname,
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.searchPostsByNicknameAndTitle(nickname, title, page, size);
    }

    //todo: 9. 사용자 닉네임을 기준으로 내용으로 검색하기
    @GetMapping("/user/{nickname}/posts/search/content")
    public Page<Post> searchPostsByNicknameAndContent(
            @PathVariable String nickname,
            @RequestParam String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.searchPostsByNicknameAndContent(nickname, content, page, size);
    }

    //todo: 사용자 닉네임을 기준으로 텍스트 검색 엔드포인트 ★★★
    @GetMapping("/user/posts/search")
    public Page<Post> searchPostsByUserUuidAndText(
            @RequestParam String nickname,
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
    HttpServletRequest request) {
        log.info("Incoming request => URI: {}, nickname: {}, text: {}",
                request.getRequestURI(), nickname, text);
        return userService.searchPostsByNicknameAndText(nickname, text, page, size);
    }
}
