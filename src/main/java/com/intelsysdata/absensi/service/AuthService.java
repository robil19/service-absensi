package com.intelsysdata.absensi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.intelsysdata.absensi.model.BiometricData;
import com.intelsysdata.absensi.model.User;
import com.intelsysdata.absensi.repository.BiometricRepository;
import com.intelsysdata.absensi.repository.UserRepository;
import com.intelsysdata.absensi.request.OnboardingRequest;
import com.intelsysdata.absensi.response.JwtResponse;
import com.intelsysdata.absensi.security.JwtUtils;
import com.intelsysdata.absensi.security.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static com.intelsysdata.absensi.common.Constants.*;

@Slf4j(topic = "AuthService")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final BiometricRepository biometricRepository;

    @Value("${app.security.jwtSecret}")
    private String jwtSecret;

    @Value("${app.security.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtResponse sigin(OnboardingRequest.LoginRequest request) {
        Authentication authentication = null;

        User userEmail = userRepository.findByEmail(request.getUsername().trim());
        JwtResponse.WorkSchedule workSchedule = new JwtResponse.WorkSchedule();
        if (userEmail != null) {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userEmail.getUsername().trim(), request.getPassword().trim()));
            workSchedule.setWorkTime(userEmail.getWorkTime());
            workSchedule.setCheckInStartTime(userEmail.getCheckInStartTime());
            workSchedule.setCheckInEndTime(userEmail.getCheckInEndTime());
            workSchedule.setCheckOutStartTime(userEmail.getCheckOutStartTime());
            workSchedule.setCheckOutEndTime(userEmail.getCheckOutEndTime());
            workSchedule.setDistanceOffice(userEmail.getDistanceOffice());
        } else {

            User userPhone = userRepository.findByPhone(request.getUsername().trim());
            if (userPhone != null) {

                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        userPhone.getUsername().trim(), request.getPassword().trim()));
                workSchedule.setWorkTime(userPhone.getWorkTime());
                workSchedule.setCheckInStartTime(userPhone.getCheckInStartTime());
                workSchedule.setCheckInEndTime(userPhone.getCheckInEndTime());
                workSchedule.setCheckOutStartTime(userPhone.getCheckOutStartTime());
                workSchedule.setCheckOutEndTime(userPhone.getCheckOutEndTime());
                workSchedule.setDistanceOffice(userPhone.getDistanceOffice());
            } else {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername().trim(), request.getPassword().trim()));
            }

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();

        log.info(LOGGED_IN_USER, new Object[] { request.getUsername() });
        return JwtResponse.builder().type("bearerToken").token(jwt).id(userDetails.getId())
                .username(userDetails.getUsername()).firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName()).isAktif(userDetails.getIsAktif()).roles(roles)
                .parameterAbsensi(workSchedule).build();
    }

    @Transactional
    public JwtResponse siginBiometric(Long userId) {
        BiometricData biometricData = biometricRepository.findByUserId(userId);
        JwtResponse.WorkSchedule workSchedule = new JwtResponse.WorkSchedule();
        if (biometricData != null) {
            User userData = userRepository.findById(biometricData.getUserId()).get();
            if (userData != null) {
                workSchedule.setWorkTime(userData.getWorkTime());
                workSchedule.setCheckInStartTime(userData.getCheckInStartTime());
                workSchedule.setCheckInEndTime(userData.getCheckInEndTime());
                workSchedule.setCheckOutStartTime(userData.getCheckOutStartTime());
                workSchedule.setCheckOutEndTime(userData.getCheckOutEndTime());
                workSchedule.setDistanceOffice(userData.getDistanceOffice());
                String jwt = Jwts.builder().setSubject((userData.getUsername())).setIssuedAt(new Date())
                        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                        .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
                ;

                final List<SimpleGrantedAuthority> authorities = userData.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getType().name())).toList();
                final List<String> roles = authorities.stream().map(item -> item.getAuthority()).toList();

                log.info(LOGGED_IN_USER, new Object[] { userData.getUsername() });
                return JwtResponse.builder().type("bearerToken").token(jwt).id(userData.getId())
                        .username(userData.getUsername()).firstName(userData.getFirstName())
                        .lastName(userData.getLastName()).roles(roles).parameterAbsensi(workSchedule).build();

            }
        }
        return null;

    }

}