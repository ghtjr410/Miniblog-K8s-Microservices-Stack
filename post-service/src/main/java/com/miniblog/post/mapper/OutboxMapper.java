package com.miniblog.post.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.model.Post;
import com.miniblog.post.util.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class OutboxMapper {
    public OutboxEvent toEntity(Post post) throws JsonProcessingException {
        try {
            // Avro로 생성된 PostCreatedEvent 객체 생성
            PostCreatedEvent postCreatedEvent = new PostCreatedEvent();
            postCreatedEvent.setPostUuid(post.getPostUuid());
            postCreatedEvent.setUserUuid(post.getUserUuid());
            postCreatedEvent.setNickname(post.getNickname());
            postCreatedEvent.setTitle(post.getTitle());
            postCreatedEvent.setContent(post.getContent());
            postCreatedEvent.setCreatedDate(post.getCreatedDate().getTime());
            postCreatedEvent.setUpdatedDate(post.getUpdatedDate().getTime());

            // Avro 객체를 JSON 문자열로 직렬화
            String payload = avroToJson(postCreatedEvent);

            return OutboxEvent.builder()
                    .eventUuId(UUID.randomUUID().toString())
                    .postUuid(post.getPostUuid())
                    .eventType("PostCreatedEvent")
                    .payload(payload)
                    .createdDate(new Date())
                    .sagaStatus(SagaStatus.CREATED)
                    .processed(false)
                    .build();

        } catch (IOException e) {
            log.error("OutboxMapper toEntity : {}", e.getMessage(), e);
            throw  new RuntimeException("OutboxEvent 생성 실패", e);
        }
    }

    private String avroToJson(PostCreatedEvent postCreatedEvent) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DatumWriter<PostCreatedEvent> datumWriter = new SpecificDatumWriter<>(PostCreatedEvent.class);
        JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(PostCreatedEvent.getClassSchema(), byteArrayOutputStream);
        datumWriter.write(postCreatedEvent, jsonEncoder);
        jsonEncoder.flush();
        byteArrayOutputStream.flush();

        return byteArrayOutputStream.toString();
    }
}
