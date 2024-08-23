package org.example.humans.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.humans.dto.LogInDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "UserID")
@Entity
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 자동으로 1씩 증가
    @Column(name = "UserID", updatable = false, unique = true, nullable = false)
    private Long UserID;

    @Column(name = "ID", nullable = false, unique = true)
    private String ID;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickName", nullable = false)
    private String nickName;

    @Builder
    public Users(String ID, String password, String nickName){
        this.ID = ID;
        this.password = password;
        this.nickName = nickName;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername(){
        return this.ID;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
