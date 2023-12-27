package com.intelsysdata.absensi.controller;

import static com.intelsysdata.absensi.common.Constants.SUCCESS;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.intelsysdata.absensi.response.ApiResponse;
import com.intelsysdata.absensi.response.AttendanceRecordResponse;
import com.intelsysdata.absensi.service.AttendanceRecordService;
import com.intelsysdata.absensi.service.AttendanceReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceRecordService attendanceRecordService;

    private final AttendanceReportService reportService;
    private final Clock clock;
    // @PostMapping("/checkin/{userId}")
    // public ResponseEntity<AttendanceRecord> checkIn(@PathVariable Long userId) {
    // return attendanceRecordService.checkIn(userId);
    // }

    // @PostMapping("/checkout/{userId}")
    // public ResponseEntity<AttendanceRecord> checkOut(@PathVariable Long userId) {
    // return attendanceRecordService.checkOut(userId);
    // }

    @PostMapping("/checkin/attendanceDate/{userId}")
    public ResponseEntity<AttendanceRecordResponse> checkIn(@PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") Date attendanceDate) {

        Optional<AttendanceRecordResponse> record = attendanceRecordService.checkInByAttendanceDate(userId,
                attendanceDate);
        if (record == null) {
            return ResponseEntity.notFound().build();
        } else {
            return record.map(ResponseEntity::ok).orElseThrow();
        }
    }

    @PostMapping("/checkout/attendanceDate/{userId}")
    public ResponseEntity<AttendanceRecordResponse> checkOut(@PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") Date attendanceDate) {
        Optional<AttendanceRecordResponse> record = attendanceRecordService.checkOutByAttendanceDate(userId,
                attendanceDate);
        if (record == null) {
            return ResponseEntity.notFound().build();
        } else {
            return record.map(ResponseEntity::ok).orElseThrow();
        }
    }

    @GetMapping("/user/{userId}/top-record")
    public ResponseEntity<AttendanceRecordResponse> getTopAttendanceRecordByUserIdAndAttendanceDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") Date attendanceDate) {
        Optional<AttendanceRecordResponse> record = attendanceRecordService
                .getTopAttendanceRecordByUserIdAndAttendanceDate(userId, attendanceDate);

        AttendanceRecordResponse dummyRecord = new AttendanceRecordResponse();
        return record.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(dummyRecord));

    }

    @PostMapping("/xlsx")
    public ResponseEntity<byte[]> generateXlsxReport() throws IOException {
        List<AttendanceRecordResponse> data = attendanceRecordService.findAll();
        byte[] report = reportService.generateXlsxReport(data);

        return createResponseEntity(report, "attendance_report.xlsx");
    }

    @PostMapping("/xls")
    public ResponseEntity<byte[]> generateXlsReport() throws IOException {
        List<AttendanceRecordResponse> data = attendanceRecordService.findAll();
        byte[] report = reportService.generateXlsReport(data);

        return createResponseEntity(report, "attendance_report.xls");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AttendanceRecordResponse>>> findAll(Pageable pageable) {
        final Page<AttendanceRecordResponse> response = attendanceRecordService.findAll(pageable);
        return ResponseEntity
                .ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response, HttpStatus.OK.value()));
    }

    private ResponseEntity<byte[]> createResponseEntity(
            byte[] report,
            String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "\"" + fileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(report);
    }
}
