package com.example.chatapp_v2.repository;

import com.example.chatapp_v2.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name); // Find groups by name
}
