package com.intelsysdata.absensi.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.intelsysdata.absensi.model.AttendanceRecord;
import com.intelsysdata.absensi.response.AttendanceRecordResponse;

@Mapper(componentModel = "spring")
public interface AttendanceRecordResponseMapper {

    AttendanceRecordResponse toDto(AttendanceRecord entity);

    @AfterMapping
    default void setFullName(@MappingTarget AttendanceRecordResponse dto, AttendanceRecord entity) {
        dto.setUserId(entity.getUser().getId());
    }
}
