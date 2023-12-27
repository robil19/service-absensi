package com.intelsysdata.absensi.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelsysdata.absensi.service.ReportService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/xlsx")
    public ResponseEntity<byte[]> generateXlsxReport() throws IOException {
        byte[] report = reportService.generateXlsxReport();

        return createResponseEntity(report, "report.xlsx");
    }

    @PostMapping("/xls")
    public ResponseEntity<byte[]> generateXlsReport() throws IOException {
        byte[] report = reportService.generateXlsReport();

        return createResponseEntity(report, "report.xls");
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
