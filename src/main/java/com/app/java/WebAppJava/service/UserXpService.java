package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.UserProfile;
import com.app.java.WebAppJava.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserXpService {

    private final UserProfileRepository userProfileRepository;

    public UserXpService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public void addXp(String username, int xpToAdd) {
        UserProfile profile = userProfileRepository.findById(username)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUsername(username);
                    p.setInstitution(null);
                    p.setXp(0);
                    return p;
                });

        profile.setXp(profile.getXp() + xpToAdd);
        userProfileRepository.save(profile);
    }
}