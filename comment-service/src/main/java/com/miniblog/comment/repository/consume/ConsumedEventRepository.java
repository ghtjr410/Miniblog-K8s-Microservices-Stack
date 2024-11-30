package com.miniblog.comment.repository.consume;

import com.miniblog.comment.model.ConsumedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsumedEventRepository extends JpaRepository<ConsumedEvent, UUID> {

}
