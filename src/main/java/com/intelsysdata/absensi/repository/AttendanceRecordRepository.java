package com.intelsysdata.absensi.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intelsysdata.absensi.model.AttendanceRecord;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findTopByUserIdOrderByTimestampCheckInDesc(Long userId);

    Optional<AttendanceRecord> findTopByUserIdAndAttendanceDateOrderByTimestampCheckInDesc(
            Long userId, Date attendanceDate);
}
