package obss.pokedex.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import obss.pokedex.user.model.UserResponse;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private Set<Role> roles;

    @ElementCollection
    private Set<UUID> wishList;

    @ElementCollection
    private Set<UUID> catchList;

    public UserResponse toUserResponse() {
        return UserResponse.builder()
                .username(this.username)
                .roles(this.roles.stream().map(Role::getName).collect(Collectors.toSet()))
                .id(id)
                .build();
    }
}
