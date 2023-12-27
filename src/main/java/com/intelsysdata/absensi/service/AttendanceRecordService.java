package com.intelsysdata.absensi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intelsysdata.absensi.mapper.AttendanceRecordResponseMapper;
import com.intelsysdata.absensi.model.AttendanceRecord;
import com.intelsysdata.absensi.repository.AttendanceRecordRepository;
import com.intelsysdata.absensi.repository.UserRepository;
import com.intelsysdata.absensi.response.AttendanceRecordResponse;

import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;

    private final UserRepository userRepository;
    private final AttendanceRecordResponseMapper attendanceRecordResponseMapper;

    public ResponseEntity<AttendanceRecord> checkIn(Long userId) {
        Optional<AttendanceRecord> lastRecord = attendanceRecordRepository
                .findTopByUserIdOrderByTimestampCheckInDesc(userId);

        if (lastRecord.isPresent() && lastRecord.get().getTimestampCheckOut() == null) {
            // Sudah melakukan Check In sebelumnya dan belum melakukan Check Out
            System.err.println("Sudah melakukan Check In sebelumnya dan belum melakukan Check Out");
            return ResponseEntity.ok(lastRecord.get());
        } else {
            // Belum melakukan Check In sebelumnya atau sudah Check Out, buat catatan baru
            AttendanceRecord record = new AttendanceRecord();
            record.setUser(userRepository.getReferenceById(userId));
            record.setType("Check In");
            record.setTimestampCheckIn(LocalTime.now());
            record.addLog("Check In recorded.");
            attendanceRecordRepository.save(record);

            return ResponseEntity.ok(record);
        }
    }

    public ResponseEntity<AttendanceRecord> checkOut(Long userId) {
        Optional<AttendanceRecord> lastRecord = attendanceRecordRepository
                .findTopByUserIdOrderByTimestampCheckInDesc(userId);

        if (lastRecord.isPresent() && lastRecord.get().getTimestampCheckOut() == null) {
            // Sudah melakukan Check In sebelumnya dan belum melakukan Check Out
            lastRecord.get().setType("Check Out");
            lastRecord.get().setTimestampCheckOut(LocalTime.now());
            lastRecord.get().addLog("Check Out recorded.");
            attendanceRecordRepository.save(lastRecord.get());

            return ResponseEntity.ok(lastRecord.get());
        } else {
            // Belum melakukan Check In sebelumnya atau sudah Check Out
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public Optional<AttendanceRecordResponse> checkInByAttendanceDate(Long userId, Date attendanceDate) {
        Optional<AttendanceRecord> lastRecord = attendanceRecordRepository
                .findTopByUserIdAndAttendanceDateOrderByTimestampCheckInDesc(userId, attendanceDate);

        if (lastRecord.isPresent() && lastRecord.get().getTimestampCheckOut() == null) {
            System.err.println("Sudah melakukan Check In sebelumnya dan belum melakukan Check Out");
            final Optional<AttendanceRecordResponse> data = lastRecord.map(attendanceRecordResponseMapper::toDto);
            return data;
            // return ResponseEntity.ok(lastRecord.get());
        } else {
            // Belum melakukan Check In sebelumnya atau sudah Check Out, buat catatan baru
            AttendanceRecord record = new AttendanceRecord();
            record.setUser(userRepository.getReferenceById(userId));
            record.setType("Check In");
            record.setTimestampCheckIn(LocalTime.now());
            record.addLog("Check In recorded.");
            record.setAttendanceDate(attendanceDate);
            attendanceRecordRepository.save(record);
            Optional<AttendanceRecordResponse> data = Optional.ofNullable(attendanceRecordResponseMapper.toDto(record));
            return data;
        }
    }

    @Transactional
    public Optional<AttendanceRecordResponse> checkOutByAttendanceDate(Long userId, Date attendanceDate) {
        Optional<AttendanceRecord> lastRecord = attendanceRecordRepository
                .findTopByUserIdAndAttendanceDateOrderByTimestampCheckInDesc(userId, attendanceDate);

        if (lastRecord.isPresent() && lastRecord.get().getTimestampCheckOut() == null) {
            // Sudah melakukan Check In sebelumnya dan belum melakukan Check Out
            lastRecord.get().setType("Check Out");
            lastRecord.get().setTimestampCheckOut(LocalTime.now());
            lastRecord.get().addLog("Check Out recorded.");
            attendanceRecordRepository.save(lastRecord.get());

            final Optional<AttendanceRecordResponse> data = lastRecord.map(attendanceRecordResponseMapper::toDto);
            return data;
            // return ResponseEntity.ok(lastRecord.get());
        } else {
            // Belum melakukan Check In sebelumnya atau sudah Check Out
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Optional<AttendanceRecordResponse> getTopAttendanceRecordByUserIdAndAttendanceDate(
            Long userId, Date attendanceDate) {

        // return attendanceRecordRepository
        // .findTopByUserIdAndAttendanceDateOrderByTimestampCheckInDesc(userId,
        // attendanceDate);
        final Optional<AttendanceRecordResponse> data = attendanceRecordRepository
                .findTopByUserIdAndAttendanceDateOrderByTimestampCheckInDesc(userId, attendanceDate)
                .map(attendanceRecordResponseMapper::toDto);

        // if (data.isEmpty())
        // throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        return data;
    }

    @Transactional(readOnly = true)
    public Page<AttendanceRecordResponse> findAll(Pageable pageable) {

        final Page<AttendanceRecordResponse> data = attendanceRecordRepository.findAll(pageable)
                .map(attendanceRecordResponseMapper::toDto);

        // if (data.isEmpty())
        // throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        return data;
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordResponse> findAll() {

        final List<AttendanceRecordResponse> data = attendanceRecordRepository.findAll()
                .stream()
                .map(attendanceRecordResponseMapper::toDto)
                .collect(Collectors.toList());
        // if (data.isEmpty())
        // throw new NoSuchElementFoundException(NOT_FOUND_RECORD);
        return data;
    }
}
