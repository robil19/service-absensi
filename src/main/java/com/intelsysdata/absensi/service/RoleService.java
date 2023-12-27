package com.intelsysdata.absensi.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.intelsysdata.absensi.model.Role;
import com.intelsysdata.absensi.model.RoleType;
import com.intelsysdata.absensi.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getReferenceByTypeIsIn(Set<RoleType> types) {
        return roleRepository.getReferenceByTypeIsIn(types);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
