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
    public Page<Post> getPostsLikedByUser(String userUuid, int page, int size) {
        List<Like> likes = likeRepository.findByUserUuid(userUuid);
        List<String> postUuids = likes.stream()
                .map(Like::getPostUuid)
                .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByPostUuidInOrderByCreatedDateDesc(postUuids, pageable);
    }

    // 2. 사용자 UUID를 기준으로 작성한 댓글 목록 가져오기
    public Page<Post> getCommentsByUser(String userUuid, int page, int size) {
        List<Comment> comments = commentRepository.findByUserUuid(userUuid);
        List<String> postUuids = comments.stream()
                .map(Comment::getPostUuid)
                .distinct() // 같은 게시글에 여러 댓글을 달았을 수도 있으므로 중복 제거
                .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByPostUuidInOrderByCreatedDateDesc(postUuids, pageable);
    }

    //todo: 3. 닉네임을 기준으로 최신순 게시글 가져오기
    public List<Post> getPostsByNickname(String nickname) {
        return postRepository.findByNicknameOrderByCreatedDateDesc(nickname);
    }
    //todo: 4. 사용자 UUID를 기준으로 작성한 게시글 최신순으로 20개씩 가져오기
    public Page<Post> getPostsByNicknameOrderByCreatedDateDesc(String nickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByNicknameOrderByCreatedDateDesc(nickname, pageable);
    }
    //todo: 5. 사용자 UUID를 기준으로 작성한 게시글 좋아요순으로 20개씩 가져오기
    public Page<Post> getPostsByNicknameOrderByLikeCountDesc(String nickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByNicknameOrderByLikeCountDesc(nickname, pageable);
    }
    //todo: 6. 사용자 UUID를 기준으로 작성한 게시글 조회수순으로 20개씩 가져오기
    public Page<Post> getPostsByNicknameOrderByTotalViewsDesc(String nickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByNicknameOrderByTotalViewsDesc(nickname, pageable);
    }
    //todo: 7. 사용자 UUID를 기준으로 프로필 정보 가져오기
    public Optional<Profile> getProfileByNickname(String nickname) {
        return profileRepository.findByNickname(nickname);
    }
    //todo: 8. 사용자 UUID를 기준으로 제목으로 검색하기
    public Page<Post> searchPostsByNicknameAndTitle(String nickname, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByNicknameAndTitleContaining(nickname, title, pageable);
    }
    //todo: 9. 사용자 UUID를 기준으로 내용으로 검색하기
    public Page<Post> searchPostsByNicknameAndContent(String nickname, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByNicknameAndContentContaining(nickname, content, pageable);
    }
    //todo:  닉네임을 기준으로 제목과 내용으로 텍스트 검색하기
    public Page<Post> searchPostsByNicknameAndText(String nickname, String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.searchByNicknameAndText(nickname, text, pageable);
    }
}
