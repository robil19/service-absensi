package com.intelsysdata.absensi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = { "type" })
@JsonIgnoreProperties(ignoreUnknown = false)

public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-role")
    @SequenceGenerator(name = "sequence-role", sequenceName = "sequence_role", allocationSize = 5)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.getRoles().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getRoles().remove(this);
    }
}
