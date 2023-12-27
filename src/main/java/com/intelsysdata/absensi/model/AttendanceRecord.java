package com.intelsysdata.absensi.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "attendance_records")
@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class AttendanceRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "type")
    private String type;

    @Column(name = "timestamp_checkin")
    private LocalTime timestampCheckIn;

    @Column(name = "timestamp_checkout")
    private LocalTime timestampCheckOut;

    @Column(name = "timestamp_create", nullable = false, updatable = false)
    private LocalTime timestampCreate;

    @Column(name = "timestamp_update")
    private LocalTime timestampUpdate;

    @Column(name = "logs")
    private List<String> logs;

    @Column(name = "attendance_date")
    @Temporal(TemporalType.DATE)
    private Date attendanceDate;

    @PrePersist
    protected void onCreate() {
        timestampCreate = LocalTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        timestampUpdate = LocalTime.now();
    }

    public void addLog(String log) {
        if (logs == null) {
            logs = new ArrayList<>();
        }
        logs.add(log);
    }

}
