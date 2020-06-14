package com.example.dbx.security.services;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String username;

    private OrgUnit orgUnit;

    private Boolean isEnabled;

    private UserRole userRole;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String name, OrgUnit orgUnit, String username, Boolean isEnabled, String password,
            UserRole userRole, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.orgUnit = orgUnit;
        this.username = username;
        this.isEnabled = isEnabled;
        this.password = password;
        this.userRole = userRole;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        System.out.println(user);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));

        List<GrantedAuthority> authorities = roles;

        return new UserPrinciple(user.getId(), user.getName(), user.getOrgUnit(), user.getUsername(),
                user.getIsEnabled(), user.getPassword(), user.getRole(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return getIsEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

    public static UserPrinciple extractFromPrincipal(Principal principal) {
        UsernamePasswordAuthenticationToken t = (UsernamePasswordAuthenticationToken) principal;
        UserPrinciple up = (UserPrinciple) t.getPrincipal();

        return up;
    }
}