package com.team4.toucheese.auth.dto;

import com.team4.toucheese.user.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] rawAuthorities = role.split(",");
        for (String rawAuthority : rawAuthorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
        }
        return grantedAuthorities;
    }

    public static CustomUserDetails fromEntity(UserEntity entity) {
        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .build();
    }

    //
//    @Override
//    public String getPassword() {return this.password;}
//
//    @Override
//    public String getUsername() {return this.email;}
//
//    @Override
//    public boolean isAccountNonExpired() {return true;}
//
//    @Override
//    public boolean isAccountNonLocked() {return true;}
//
//    @Override
//    public boolean isCredentialsNonExpired() {return true;}
//
//    @Override
//    public boolean isEnabled() {return true;}
}
