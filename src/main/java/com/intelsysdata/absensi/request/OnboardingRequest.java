package com.intelsysdata.absensi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OnboardingRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupRequest {

        @Email(message = "email.acceptable")
        @Size(min = 6, max = 50, message = "{email.size}")
        @NotBlank(message = "email.notblank}")
        private String email;

        @NotBlank(message = "phone.notblank}")
        private String phone;

        @Size(min = 6, max = 100, message = "{password.size}")
        @NotBlank(message = "password.notblank}")
        private String password;
        private String kodeReferral;
        private Integer pin;
        private String firstName;
        private String lastName;
        private int workTime; // in minutes
        private int checkInStartTime; // in minutes
        private int checkInEndTime; // in minutes
        private int checkOutStartTime; // in minutes
        private int checkOutEndTime; // in minutes
        private double distanceOffice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {

        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }
}
