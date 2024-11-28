package com.miniblog.query.controller.post;

import com.miniblog.query.dto.PostDetailDTO;
import com.miniblog.query.model.Post;
import com.miniblog.query.service.post.PostQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/query")
@RequiredArgsConstructor
@Slf4j
public class PostQueryController {
    private final PostQueryService basicService;

    // 10. 전체 게시글 최신순으로 40개씩 가져오기
    @GetMapping("/posts/latest")
    public Page<Post> getAllPostsOrderByCreatedDateDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size) {
        return basicService.getAllPostsOrderByCreatedDateDesc(page, size);
    }
    // 11. 전체 게시글 좋아요순으로 40개씩 가져오기
    @GetMapping("/posts/most-liked")
    public Page<Post> getAllPostsOrderByLikeCountDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size) {
        return basicService.getAllPostsOrderByLikeCountDesc(page, size);
    }
    // 12. 전체 게시글 조회수순으로 40개씩 가져오기
    @GetMapping("/posts/most-viewed")
    public Page<Post> getAllPostsOrderByViewCountDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size) {
        return basicService.getAllPostsOrderByViewCountDesc(page, size);
    }
    // 13. 전체 범위 제목으로 검색하기
    @GetMapping("/posts/search/title")
    public Page<Post> searchAllPostsByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size) {
        return basicService.searchAllPostsByTitle(title, page, size);
    }
    // 14. 전체 범위 내용으로 검색하기
    @GetMapping("/posts/search/content")
    public Page<Post> searchAllPostsByContent(
            @RequestParam String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size) {
        return basicService.searchAllPostsByContent(content, page, size);
    }
    // 전체 범위 텍스트 검색 엔드포인트
    @GetMapping("/posts/search")
    public Page<Post> searchAllPostsByText(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size) {
        return basicService.searchAllPostsByText(text, page, size);
    }
    // 15. 게시글 UUID를 기준으로 해당 게시글 정보와 연관된 댓글 가져오기
    @GetMapping("/posts/{postUuid}")
    public ResponseEntity<PostDetailDTO> getPostDetail(@PathVariable String postUuid) {
        Optional<PostDetailDTO> postDetail = basicService.getPostDetail(postUuid);
        return postDetail.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
