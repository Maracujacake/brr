package web.brr.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class DefaultSucessLogin implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, Authentication authentication)
            throws IOException, jakarta.servlet.ServletException {

        // Get the roles of the logged-in user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = null;

        // Check user role and set the redirect URL
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_CLIENTE")) {
                redirectUrl = "/cliente/";
                break;
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/";
                break;
            } else if (authority.getAuthority().equals("ROLE_LOCADORA")) {
                redirectUrl = "/locadora/";
                break;
            }
            // Add more roles and URLs as needed
        }

        if (redirectUrl == null) {
            throw new IllegalStateException();
        }

        response.sendRedirect(redirectUrl);
    }
}
