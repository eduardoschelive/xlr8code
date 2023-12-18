package com.xlr8code.server.domain.user.repository;

import com.xlr8code.server.domain.user.entity.UserMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserMetadataRepository extends JpaRepository<UserMetadata, UUID> {

}
