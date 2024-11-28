package com.miniblog.query.repository.post;

import com.miniblog.query.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String>, PostIncrementOperations, PostDecrementOperations, PostUpdateOperations {
    Post findByPostUuid(String postUuid);
    List<Post> findByUserUuid(String userUuid);
    Page<Post> findByUserUuidOrderByCreatedDateDesc(String userUuid, Pageable pageable);
    Page<Post> findByUserUuidOrderByLikeCountDesc(String userUuid, Pageable pageable);
    Page<Post> findByUserUuidOrderByViewCountDesc(String userUuid, Pageable pageable);
    Page<Post> findByUserUuidAndTitleContaining(String userUuid, String title, Pageable pageable);
    Page<Post> findByUserUuidAndContentContaining(String userUuid, String content, Pageable pageable);

    Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<Post> findAllByOrderByLikeCountDesc(Pageable pageable);
    Page<Post> findAllByOrderByViewCountDesc(Pageable pageable);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
    Page<Post> findByContentContaining(String content, Pageable pageable);
    void deleteByUserUuid(String userUuid);
//    Optional<Post> findById(String postUuid);
// 전체 범위 텍스트 검색
    @Query("{'$text': {'$search': ?0}}")
    Page<Post> searchByText(String text, Pageable pageable);

    // 사용자 UUID를 기준으로 텍스트 검색
    @Query("{'userUuid': ?0, '$text': {'$search': ?1}}")
    Page<Post> searchByUserUuidAndText(String userUuid, String text, Pageable pageable);
}
