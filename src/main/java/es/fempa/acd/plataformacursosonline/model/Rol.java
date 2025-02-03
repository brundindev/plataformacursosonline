package es.fempa.acd.plataformacursosonline.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public enum Rol {
    ADMIN, PROFESOR, ESTUDIANTE;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + this.name());
    }
}
