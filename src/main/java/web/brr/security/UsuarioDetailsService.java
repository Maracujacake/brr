package web.brr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import web.brr.domains.User;
import web.brr.repositories.UserRep;

public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UserRep dao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User usuario = dao.findByEmail(username).orElse(null);

        if (usuario == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new UsuarioDetails(usuario);
    }
}
