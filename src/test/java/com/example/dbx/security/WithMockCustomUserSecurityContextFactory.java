package com.example.dbx.security;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.security.services.UserPrinciple;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserRole role = customUser.role().contains("USER") ? UserRole.ROLE_USER : UserRole.ROLE_ADMIN;

        UserPrinciple principal = UserPrinciple
                .build(User.builder().name(customUser.name()).username(customUser.username()).role(role)
                        .orgUnit(new OrgUnit(customUser.orgUnitId(), "name")).password("pass").build());

        System.out.println("PRICPAL:- " + principal);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password",
                principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}