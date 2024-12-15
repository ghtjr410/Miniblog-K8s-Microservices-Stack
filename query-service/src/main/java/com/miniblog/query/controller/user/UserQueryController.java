package com.miniblog.query.controller.user;

import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Post;
import com.miniblog.query.model.Profile;
import com.miniblog.query.service.user.UserQueryService;
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

    // 1. 사용자 UUID를 기준으로 좋아요 누른 게시글 목록 가져오기
    @GetMapping("/user/{userUuid}/liked-posts")
    public List<Post> getPostsLikedByUser(@PathVariable String userUuid) {
        return userService.getPostsLikedByUser(userUuid);
    }
    // 2. 사용자 UUID를 기준으로 작성한 댓글 목록 가져오기
    @GetMapping("/user/{userUuid}/comments")
    public List<Comment> getCommentsByUser(@PathVariable String userUuid) {
        return userService.getCommentsByUser(userUuid);
    }
    // 3. 사용자 UUID를 기준으로 작성한 게시글 모든 목록 가져오기
    @GetMapping("/user/{userUuid}/posts")
    public List<Post> getPostsByUser(@PathVariable String userUuid) {
        return userService.getPostsByUser(userUuid);
    }
    // 4. 사용자 UUID를 기준으로 작성한 게시글 최신순으로 20개씩 가져오기
    @GetMapping("/user/{userUuid}/posts/latest")
    public Page<Post> getPostsByUserOrderByCreatedDateDesc(
            @PathVariable String userUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsByUserOrderByCreatedDateDesc(userUuid, page, size);
    }
    //5. 사용자 UUID를 기준으로 작성한 게시글 좋아요순으로 20개씩 가져오기
    @GetMapping("/user/{userUuid}/posts/most-liked")
    public Page<Post> getPostsByUserOrderByLikeCountDesc(
            @PathVariable String userUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsByUserOrderByLikeCountDesc(userUuid, page, size);
    }
    // 6. 사용자 UUID를 기준으로 작성한 게시글 조회수순으로 20개씩 가져오기
    @GetMapping("/user/{userUuid}/posts/most-viewed")
    public Page<Post> getPostsByUserOrderByViewCountDesc(
            @PathVariable String userUuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.getPostsByUserOrderByViewCountDesc(userUuid, page, size);
    }
    // 7. 사용자 UUID를 기준으로 프로필 정보 가져오기
    @GetMapping("/user/{userUuid}/profile")
    public ResponseEntity<Profile> getProfileByUserUuid(@PathVariable String userUuid) {
        Optional<Profile> profile = userService.getProfileByUserUuid(userUuid);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // 8. 사용자 UUID를 기준으로 제목으로 검색하기
    @GetMapping("/user/{userUuid}/posts/search/title")
    public Page<Post> searchPostsByUserAndTitle(
            @PathVariable String userUuid,
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.searchPostsByUserAndTitle(userUuid, title, page, size);
    }
    // 사용자 UUID를 기준으로 텍스트 검색 엔드포인트
    @GetMapping("/user/{userUuid}/posts/search")
    public Page<Post> searchPostsByUserUuidAndText(
            @PathVariable String userUuid,
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.searchPostsByUserUuidAndText(userUuid, text, page, size);
    }
    // 9. 사용자 UUID를 기준으로 내용으로 검색하기
    @GetMapping("/user/{userUuid}/posts/search/content")
    public Page<Post> searchPostsByUserAndContent(
            @PathVariable String userUuid,
            @RequestParam String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.searchPostsByUserAndContent(userUuid, content, page, size);
    }
}
