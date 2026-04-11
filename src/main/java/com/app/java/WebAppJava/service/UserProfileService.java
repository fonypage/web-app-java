package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.dto.UserProfileSummaryDto;

public interface UserProfileService {
    UserProfileSummaryDto getSummary(String username);
    void updateInstitution(String username, String institution);
    void updateProfile(String username, String institution, String displayName);
}