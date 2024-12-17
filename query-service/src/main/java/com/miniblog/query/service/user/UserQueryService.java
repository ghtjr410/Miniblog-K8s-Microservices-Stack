package com.miniblog.query.service.user;

import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Like;
import com.miniblog.query.model.Post;
import com.miniblog.query.model.Profile;
import com.miniblog.query.repository.comment.CommentRepository;
import com.miniblog.query.repository.like.LikeRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.repository.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;

    // 1. 사용자 UUID를 기준으로 좋아요 누른 게시글 목록 가져오기
    public List<Post> getPostsLikedByUser(String userUuid) {
        List<Like> likes = likeRepository.findByUserUuid(userUuid);
        List<String> postUuids = likes.stream()
                .map(Like::getPostUuid)
                .collect(Collectors.toList());
        return postRepository.findAllById(postUuids);
    }

   // 2. 사용자 UUID를 기준으로 작성한 댓글 목록 가져오기
    public List<Comment> getCommentsByUser(String userUuid) {
        return commentRepository.findByUserUuid(userUuid);
    }

    //3. 사용자 UUID를 기준으로 작성한 게시글 모든 목록 가져오기
    public List<Post> getPostsByUser(String userUuid) {
        return postRepository.findByUserUuid(userUuid);
    }

    //4. 사용자 UUID를 기준으로 작성한 게시글 최신순으로 20개씩 가져오기
    public Page<Post> getPostsByUserOrderByCreatedDateDesc(String userUuid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserUuidOrderByCreatedDateDesc(userUuid, pageable);
    }
    //5. 사용자 UUID를 기준으로 작성한 게시글 좋아요순으로 20개씩 가져오기
    public Page<Post> getPostsByUserOrderByLikeCountDesc(String userUuid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserUuidOrderByLikeCountDesc(userUuid, pageable);
    }
    //6. 사용자 UUID를 기준으로 작성한 게시글 조회수순으로 20개씩 가져오기
    public Page<Post> getPostsByUserOrderByTotalViewsDesc(String userUuid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserUuidOrderByTotalViewsDesc(userUuid, pageable);
    }
    //7. 사용자 UUID를 기준으로 프로필 정보 가져오기
    public Optional<Profile> getProfileByUserUuid(String userUuid) {
        return profileRepository.findByUserUuid(userUuid);
    }
    //8. 사용자 UUID를 기준으로 제목으로 검색하기
    public Page<Post> searchPostsByUserAndTitle(String userUuid, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserUuidAndTitleContaining(userUuid, title, pageable);
    }
    //9. 사용자 UUID를 기준으로 내용으로 검색하기
    public Page<Post> searchPostsByUserAndContent(String userUuid, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByUserUuidAndContentContaining(userUuid, content, pageable);
    }
    // 사용자 UUID를 기준으로 제목과 내용으로 텍스트 검색하기
    public Page<Post> searchPostsByUserUuidAndText(String userUuid, String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.searchByUserUuidAndText(userUuid, text, pageable);
    }
}
