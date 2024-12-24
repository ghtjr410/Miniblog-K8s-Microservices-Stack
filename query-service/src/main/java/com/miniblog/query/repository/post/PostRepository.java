package com.miniblog.query.repository.post;

import com.miniblog.query.model.Comment;
import com.miniblog.query.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String>, PostIncrementOperations, PostDecrementOperations, PostUpdateOperations {
//    Post findByPostUuid(String postUuid);
    Page<Post> findByPostUuidInOrderByCreatedDateDesc(List<String> postUuids, Pageable pageable);

    List<Post> findByNicknameOrderByCreatedDateDesc(String nickname); //todo:
    Page<Post> findByNicknameOrderByCreatedDateDesc(String userUuid, Pageable pageable); //todo:
    Page<Post> findByNicknameOrderByLikeCountDesc(String userUuid, Pageable pageable); //todo:
    Page<Post> findByNicknameOrderByTotalViewsDesc(String userUuid, Pageable pageable); //todo:
    Page<Post> findByNicknameAndTitleContaining(String userUuid, String title, Pageable pageable); //todo:
    Page<Post> findByNicknameAndContentContaining(String userUuid, String content, Pageable pageable); //todo:

    Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<Post> findAllByOrderByLikeCountDesc(Pageable pageable);
    Page<Post> findAllByOrderByTotalViewsDesc(Pageable pageable);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
    Page<Post> findByContentContaining(String content, Pageable pageable);
    void deleteByUserUuid(String userUuid);
    // 전체 범위 텍스트 검색
    @Query("{ $or: [ "
            + "  { 'title': { $regex: ?0, $options: 'i' } }, "
            + "  { 'plainContent': { $regex: ?0, $options: 'i' } } "
            + "] }")
    Page<Post> searchByText(String text, Pageable pageable);

    // 사용자 닉네임을 기준으로 텍스트 검색
    @Query("{ $and: [ "
            + "  { 'nickname': ?0 }, "
            + "  { $or: [ "
            + "    { 'title':   { $regex: ?1, $options: 'i' } }, "
            + "    { 'plainContent': { $regex: ?1, $options: 'i' } } "
            + "  ] } "
            + "] }")
    Page<Post> searchByNicknameAndText(String nickname, String regex, Pageable pageable); //todo:
}
