package com.nys.nys_backend.controller;

import com.nys.nys_backend.dto.AuthResponse;
import com.nys.nys_backend.dto.ConnexionRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final CookieCsrfTokenRepository csrfTokenRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            SessionAuthenticationStrategy sessionAuthenticationStrategy,
            CookieCsrfTokenRepository csrfTokenRepository) {

        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken csrfToken) {
        return Map.of("token", csrfToken.getToken());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody ConnexionRequest demande,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        demande.email().trim().toLowerCase(),
                        demande.motDePasse()
                    )
                );
            request.getSession(true);
            sessionAuthenticationStrategy.onAuthentication(
                authentication, request, response
            );

            SecurityContext context =
                SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(
                context, request, response
            );

            csrfTokenRepository.saveToken(null, request, response);

            UserDetails utilisateur =
                (UserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(
                new AuthResponse(
                    "Connexion réussie",
                    utilisateur.getUsername(),
                    "ADMIN"
                )
            );

        } catch (AuthenticationException exception) {
            SecurityContextHolder.clearContext();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(
                    "Identifiants incorrects",
                    "",
                    ""
                ));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> utilisateurConnecte(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Non connecté"));
        }

        return ResponseEntity.ok(
            new AuthResponse(
                "Session active",
                authentication.getName(),
                "ADMIN"
            )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {

        new SecurityContextLogoutHandler().logout(
            request, response, authentication
        );

        csrfTokenRepository.saveToken(null, request, response);

        return ResponseEntity.ok(
            Map.of("message", "Déconnexion réussie")
        );
    }
}
