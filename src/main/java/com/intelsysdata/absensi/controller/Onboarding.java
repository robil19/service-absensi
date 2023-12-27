package com.intelsysdata.absensi.controller;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelsysdata.absensi.exception.ElementAlreadyExistsException;
import com.intelsysdata.absensi.model.BiometricData;
import com.intelsysdata.absensi.model.Role;
import com.intelsysdata.absensi.model.RoleType;
import com.intelsysdata.absensi.model.User;
import com.intelsysdata.absensi.repository.BiometricRepository;
import com.intelsysdata.absensi.repository.UserRepository;
import com.intelsysdata.absensi.request.OnboardingRequest;
import com.intelsysdata.absensi.response.ApiResponse;
import com.intelsysdata.absensi.response.JwtResponse;
import com.intelsysdata.absensi.service.AuthService;
import com.intelsysdata.absensi.service.RoleService;
import static com.intelsysdata.absensi.common.Constants.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class Onboarding {

    private final UserRepository userRepository;
    private final Clock clock;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthService authService;
    private final BiometricRepository biometricRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody OnboardingRequest.LoginRequest request) {
        final JwtResponse response = authService.sigin(request);
        return ResponseEntity
                .ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response, HttpStatus.OK.value()));
    }

    @PostMapping("/login/biometric")
    public ResponseEntity<ApiResponse<JwtResponse>> loginBiometric(@Valid @RequestParam Long userId) {
        final JwtResponse response = authService.siginBiometric(userId);
        return ResponseEntity
                .ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response, HttpStatus.OK.value()));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<OnboardingRequest.SignupRequest>> signup(
            @Valid @RequestBody OnboardingRequest.SignupRequest request, final HttpServletRequest httpServletRequest) {
        if (userRepository.existsByPhoneIgnoreCase(request.getPhone().trim()))
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_USER_PHONE);

        if (userRepository.existsByEmailIgnoreCase(request.getEmail().trim()))
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_USER_EMAIL);

        final User user = new User();
        // phone use username
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getPhone());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPin(request.getPin());
        final List<RoleType> roleTypes = Arrays.asList(RoleType.ROLE_USER);
        final List<Role> roles = roleService.getReferenceByTypeIsIn(new HashSet<>(roleTypes));
        user.setRoles(new HashSet<>(roles));
        user.setWorkTime(request.getWorkTime());
        user.setCheckInStartTime(request.getCheckInStartTime());
        user.setCheckInEndTime(request.getCheckInEndTime());
        user.setCheckOutStartTime(request.getCheckOutStartTime());
        user.setCheckOutEndTime(request.getCheckOutEndTime());
        user.setDistanceOffice(request.getDistanceOffice());
        userRepository.save(user);
        BiometricData biometricData = new BiometricData();
        biometricData.setUserId(user.getId());
        biometricRepository.save(biometricData);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, request, HttpStatus.CREATED.value()));
    }

}