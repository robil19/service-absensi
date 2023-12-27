package com.intelsysdata.absensi.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private String type;
    private String token;
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean isAktif;
    private List<String> roles;
    private WorkSchedule parameterAbsensi;

    @Data
    public static class WorkSchedule {
        private int workTime; // in minutes
        private int checkInStartTime; // in minutes
        private int checkInEndTime; // in minutes
        private int checkOutStartTime; // in minutes
        private int checkOutEndTime; // in minutes
        private double distanceOffice; // in kilometers (assuming it's a double)

    }
}
