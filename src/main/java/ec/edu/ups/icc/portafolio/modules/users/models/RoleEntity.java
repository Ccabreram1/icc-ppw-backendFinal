package ec.edu.ups.icc.portafolio.modules.users.models;

import ec.edu.ups.icc.portafolio.shared.entities.BaseModel;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseModel {

    @Column(nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(length = 200)
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();

    // Constructores
    public RoleEntity() {
    }

    public RoleEntity(RoleName name) {
        this.name = name;
    }

    public RoleEntity(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}