package org.example.progetto_gestionale_cv_server.utility.generateToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTAuthFilter extends OncePerRequestFilter {

    private GenerateToken generateToken;

    public JWTAuthFilter(GenerateToken generateToken) {
        this.generateToken = generateToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            String username = this.generateToken.ExtractUsername(token);
            String role = this.generateToken.getRoleFromToken(token);
//            String userId = this.generateToken.getIdFromToken(token);

            if (username == null ||
                    role == null
                //  userId == null
            ) {
                System.out.println(" ERRORE: qualcuno dei dati estratti dal token risulta privo di valore o null.");
            }

//            if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        // all oggetto UsernamePasswordAuthenticationToken,
//                        // oltre ai valori di default (username , credentials , authorities )
//                        // posso passare valori custom che posso comunque riprendere nel controller tramite l'oggetto
//                        //  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                        new UsernamePasswordAuthenticationToken(username, null, List.of(() -> "ROLE_" + role));
//                authenticationToken.setDetails(new CustomWebAuthenticationDetailsSource().buildDetails(request, userId));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(() -> "ROLE_" + role));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
