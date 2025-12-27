package com.disha.fintrack.repository;

import com.disha.fintrack.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {


    Optional<ProfileEntity> findByEmail(String email);


    boolean existsByEmail(String email);

    List<ProfileEntity> findByIsActiveTrue();


    List<ProfileEntity> findByIsActiveFalse();

// select * from tbl_profiles where activation_token=?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
