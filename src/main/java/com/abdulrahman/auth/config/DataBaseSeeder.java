package com.abdulrahman.auth.config;

import com.abdulrahman.auth.entities.Role;
import com.abdulrahman.auth.repos.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataBaseSeeder {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void insert(){
        Role userRole= Role.builder().name("ROLE_USER").build();
        Role adminRole=Role.builder().name("ROLE_ADMIN").build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
    }

}