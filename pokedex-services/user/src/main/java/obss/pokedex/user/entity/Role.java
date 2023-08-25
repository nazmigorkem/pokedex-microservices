package obss.pokedex.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ROLE")
@org.hibernate.annotations.Cache(region = "role", usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Role {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
