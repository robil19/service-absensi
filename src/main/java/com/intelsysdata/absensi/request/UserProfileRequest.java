package com.intelsysdata.absensi.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserProfileRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private Long userid;
        private String namaLengkap;
        private MultipartFile fotoDiri;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long userid;
        private String namaLengkap;
        private String fotoDiri;
    }
}