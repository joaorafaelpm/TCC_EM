package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`group`")
public class Group {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name ;

    @ManyToMany
    @JoinTable(name = "group_permission" ,
            joinColumns = @JoinColumn(name = "group_id") ,
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permission = new HashSet<>();

    public boolean associatePermission (Permission permission) {
        return getPermission().add(permission);
    }
    public boolean disassociatePermission(Permission permission) {
        return getPermission().remove(permission);
    }


}
