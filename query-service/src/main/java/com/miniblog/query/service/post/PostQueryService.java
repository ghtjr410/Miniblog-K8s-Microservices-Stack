package com.miniblog.query.service.post;

import com.miniblog.query.dto.PostDetailDTO;
import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Post;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostQueryService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 10. 전체 게시글 최신순으로 40개씩 가져오기
    public Page<Post> getAllPostsOrderByCreatedDateDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByCreatedDateDesc(pageable);
    }
    // 11. 전체 게시글 좋아요순으로 40개씩 가져오기
    public Page<Post> getAllPostsOrderByLikeCountDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByLikeCountDesc(pageable);
    }
    // 12. 전체 게시글 조회수순으로 40개씩 가져오기
    public Page<Post> getAllPostsOrderByViewCountDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByViewCountDesc(pageable);
    }
    // 13. 전체 범위 제목으로 검색하기
    public Page<Post> searchAllPostsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByTitleContaining(title, pageable);
    }
    // 14. 전체 범위 내용으로 검색하기
    public Page<Post> searchAllPostsByContent(String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByContentContaining(content, pageable);
    }
    // 전체 범위 제목과 내용으로 텍스트 검색하기
    public Page<Post> searchAllPostsByText(String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.searchByText(text, pageable);
    }
    // 15. 게시글 UUID를 기준으로 해당 게시글 정보와 연관된 댓글 가져오기
    public Optional<PostDetailDTO> getPostDetail(String postUuid) {
        Optional<Post> postOptional = postRepository.findById(postUuid);
        if (postOptional.isEmpty()) {
            return Optional.empty();
        }
        Post post = postOptional.get();
        List<Comment> comments = commentRepository.findByPostUuidOrderByCreatedDateAsc(postUuid);

        PostDetailDTO postDetail = new PostDetailDTO(post, comments);
        return Optional.of(postDetail);
    }

}
