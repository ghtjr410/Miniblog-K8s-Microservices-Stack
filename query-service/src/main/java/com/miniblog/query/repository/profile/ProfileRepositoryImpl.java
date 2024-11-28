package com.miniblog.query.repository.profile;

import com.miniblog.query.model.Profile;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileOperations{
    private final MongoOperations mongoOperations;

    @Override
    public boolean updateProfile(String profileUuid, String title, String intro) {
        Query query = new Query(Criteria.where("profileUuid").is(profileUuid));
        Update update = new Update()
                .set("title", title)
                .set("intro", intro);
        UpdateResult result = mongoOperations.updateFirst(query, update, Profile.class);
        return result.getModifiedCount() > 0;
    }
}
