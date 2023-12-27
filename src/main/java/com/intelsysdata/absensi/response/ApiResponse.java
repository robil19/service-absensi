package com.intelsysdata.absensi.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private Long timestamp;
    private final String message;
    private final T data;
    private final int statusCode;

    public ApiResponse(Long timestamp, String message, int statusCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.data = null;
        this.statusCode = statusCode;
    }
}
