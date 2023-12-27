package com.intelsysdata.absensi.mapper;

import java.util.HashSet;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.intelsysdata.absensi.model.Role;
import com.intelsysdata.absensi.model.RoleType;
import com.intelsysdata.absensi.model.User;
import com.intelsysdata.absensi.request.SignupRequest;
import com.intelsysdata.absensi.service.RoleService;

@Mapper(componentModel = "spring", uses = { PasswordEncoder.class,
        RoleService.class }, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SignupRequestMapper {

    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Mapping(target = "firstName", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getFirstName()))")
    @Mapping(target = "lastName", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getLastName()))")
    @Mapping(target = "username", expression = "java(dto.getUsername().trim().toLowerCase())")
    @Mapping(target = "email", expression = "java(dto.getEmail().trim().toLowerCase())")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "phone", expression = "java(dto.getPhone().trim())")
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "jenisKelamin", ignore = true)
    @Mapping(target = "pekerjaan", ignore = true)
    @Mapping(target = "pin", ignore = true)
    @Mapping(target = "tanggalLahir", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract User toEntity(SignupRequest dto);

    @AfterMapping
    void setToEntityFields(@MappingTarget User entity, SignupRequest dto) {
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        final List<RoleType> roleTypes = dto.getRoles().stream().map(RoleType::valueOf).toList();
        final List<Role> roles = roleService.getReferenceByTypeIsIn(new HashSet<>(roleTypes));
        entity.setRoles(new HashSet<>(roles));
    }
}
