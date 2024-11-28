package com.miniblog.query.repository.profile;

public interface ProfileOperations {
    boolean updateProfile(String profileUuid, String title, String intro);
}