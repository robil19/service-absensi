package com.intelsysdata.absensi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intelsysdata.absensi.model.Role;
import com.intelsysdata.absensi.model.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> getReferenceByTypeIsIn(Set<RoleType> types);
}
