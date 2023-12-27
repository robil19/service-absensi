package com.intelsysdata.absensi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intelsysdata.absensi.model.BiometricData;

public interface BiometricRepository extends JpaRepository<BiometricData, Long> {
    BiometricData findByUserId(Long userId);
}
