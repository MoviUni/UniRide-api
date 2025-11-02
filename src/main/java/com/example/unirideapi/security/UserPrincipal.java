package com.example.unirideapi.security;


import com.example.unirideapi.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private Integer id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Usuario usuario;

    // ✅ Constructor que recibe un Usuario y configura los detalles necesarios
    public UserPrincipal(Usuario usuario) {
        this.usuario = usuario;
        this.id = usuario.getIdUsuario();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(usuario.getRol().getName().name())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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