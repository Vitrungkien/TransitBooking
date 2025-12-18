package com.OneBpy.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"store", "orderList"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_tb")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Store store;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }


    @Override
    @JsonIgnore
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
