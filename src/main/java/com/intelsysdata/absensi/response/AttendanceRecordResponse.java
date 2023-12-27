package com.intelsysdata.absensi.response;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AttendanceRecordResponse {
    private Long id;
    private long userId;
    private String type;
    private LocalTime timestampCheckIn;
    private LocalTime timestampCheckOut;
    private LocalTime timestampCreate;
    private LocalTime timestampUpdate;
    private List<String> logs;
    private Date attendanceDate;
    private UserResponse user;

    public void addLog(String log) {
        if (logs == null) {
            logs = new ArrayList<>();
        }
        logs.add(log);
    }
}
