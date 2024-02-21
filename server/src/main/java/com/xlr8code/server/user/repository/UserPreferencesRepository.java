package com.xlr8code.server.user.repository;

import com.xlr8code.server.user.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, UUID> {


}
