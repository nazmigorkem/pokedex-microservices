package obss.pokedex.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import obss.pokedex.user.exception.ServiceException;
import obss.pokedex.user.model.PokemonResponse;
import obss.pokedex.user.model.UserResponse;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(unique = true, nullable = false)
    private UUID keycloakId;

    @Column(unique = true, nullable = false)
    private String username;

    @ElementCollection
    private Set<UUID> wishList;

    @ElementCollection
    private Set<UUID> catchList;

    public UserResponse toUserResponse() {
        return UserResponse.builder()
                .username(this.username)
                .id(id)
                .build();
    }

    public void addPokemonToCatchList(PokemonResponse pokemon) {
        if (this.getCatchList() == null) {
            this.setCatchList(new HashSet<>());
        } else {
            if (this.getCatchList().contains(pokemon.getId())) {
                throw ServiceException.PokemonAlreadyInCatchList(pokemon.getName());
            }
        }

        this.getCatchList().add(pokemon.getId());
    }

    public void addPokemonToWishList(PokemonResponse pokemon) {
        if (this.getWishList() == null) {
            this.setWishList(new HashSet<>());
        } else {
            if (this.getWishList().contains(pokemon.getId())) {
                throw ServiceException.PokemonAlreadyInWishList(pokemon.getName());
            }
        }

        this.getWishList().add(pokemon.getId());
    }

    public void removePokemonFromCatchList(PokemonResponse pokemon) {
        if (this.getCatchList() == null || !this.getCatchList().contains(pokemon.getId())) {
            throw ServiceException.PokemonIsNotInCatchList(pokemon.getName());
        }

        this.getCatchList().remove(pokemon.getId());
    }

    public void removePokemonFromWishList(PokemonResponse pokemon) {
        if (this.getWishList() == null || !this.getWishList().contains(pokemon.getId())) {
            throw ServiceException.PokemonIsNotInWishList(pokemon.getName());
        }

        this.getWishList().remove(pokemon.getId());
    }
}

