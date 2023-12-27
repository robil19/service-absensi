package com.intelsysdata.absensi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = { "username" })
@Table(name = "user", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = false)

public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-user")
    @SequenceGenerator(name = "sequence-user", sequenceName = "sequence_user", allocationSize = 5)

    private Long id;
    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 60)

    private String password;

    private String phone;

    private boolean enabled = false;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    private String jenisKelamin;
    private String tanggalLahir;
    private String pekerjaan;
    private Integer pin;
    @Embedded
    private Avatar avatar;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    private int workTime; // in minutes
    private int checkInStartTime; // in minutes
    private int checkInEndTime; // in minutes
    private int checkOutStartTime; // in minutes
    private int checkOutEndTime; // in minutes
    private double distanceOffice; // in meters (assuming it's a double)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AttendanceRecord> attendanceRecords = new HashSet<>();

    public void addAttendanceRecord(AttendanceRecord attendanceRecord) {
        attendanceRecords.add(attendanceRecord);
        attendanceRecord.setUser(this);
    }

    public void removeAttendanceRecord(AttendanceRecord attendanceRecord) {
        attendanceRecords.remove(attendanceRecord);
        attendanceRecord.setUser(null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

}