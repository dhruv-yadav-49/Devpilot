package devPilot.backend.security;

import devPilot.backend.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    public AppUserPrincipal get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUserPrincipal appUserPrincipal) {
            return appUserPrincipal;
        }
        return null;
    }

    public AppUserPrincipal require() {
        AppUserPrincipal principal = get();
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }
        return principal;
    }
}
