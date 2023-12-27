package com.intelsysdata.absensi.controller;

import static com.intelsysdata.absensi.common.Constants.SUCCESS;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intelsysdata.absensi.mapper.UserProfileMapper;
import com.intelsysdata.absensi.model.Avatar;
import com.intelsysdata.absensi.model.User;
import com.intelsysdata.absensi.repository.UserRepository;
import com.intelsysdata.absensi.request.UserProfileRequest;
import com.intelsysdata.absensi.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final Clock clock;
    private final UserProfileMapper userProfileMapper;

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<UserProfileRequest.Response>> updateImages(
            @ModelAttribute UserProfileRequest.Request request,
            final HttpServletRequest httpServletRequest) throws IOException {

        MultipartFile image = request.getFotoDiri();
        if (image != null && !image.isEmpty()) {
            User user = userRepository.findById(request.getUserid()).get();
            String filename = image.getOriginalFilename();
            byte[] fileData = image.getBytes();
            Avatar dataImage = new Avatar();
            dataImage.setFilename(filename);
            dataImage.setUrl(null);
            dataImage.setFileData(fileData);
            user.setAvatar(dataImage);
            userRepository.save(user);
            UserProfileRequest.Response data = userProfileMapper.toResponse(request);
            data.setFotoDiri(user.getAvatar().getUrl());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, data, HttpStatus.OK.value()));
        }
        return null;

    }

    @GetMapping("/images/{userId}")
    public ResponseEntity<byte[]> getImageByKode(@PathVariable Long userId) throws IOException {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Avatar avatar = user.getAvatar();

            if (avatar != null) {
                byte[] imageData = avatar.getFileData();

                if (imageData != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
                } else {
                    ClassPathResource defaultImageResource = new ClassPathResource("static/images/no_user.jpg");
                    byte[] imageBytes = StreamUtils.copyToByteArray(defaultImageResource.getInputStream());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
                }
            } else {
                ClassPathResource defaultImageResource = new ClassPathResource("static/images/no_user.jpg");
                byte[] imageBytes = StreamUtils.copyToByteArray(defaultImageResource.getInputStream());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
