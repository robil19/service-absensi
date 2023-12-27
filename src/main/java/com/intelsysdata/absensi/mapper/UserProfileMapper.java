package com.intelsysdata.absensi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.intelsysdata.absensi.request.UserProfileRequest;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    @Mapping(target = "fotoDiri", ignore = true)
    UserProfileRequest.Response toResponse(UserProfileRequest.Request data);

    @Mapping(target = "fotoDiri", ignore = true)
    UserProfileRequest.Request toRequest(UserProfileRequest.Response data);

}