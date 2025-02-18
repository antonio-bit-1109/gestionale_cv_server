package org.example.progetto_gestionale_cv_server.utility.configuration;

import org.example.progetto_gestionale_cv_server.utility.generateToken.GenerateToken;
import org.example.progetto_gestionale_cv_server.utility.generateToken.JWTAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class Configurations {

    private final GenerateToken generateToken;

    public Configurations(GenerateToken genToken) {
        this.generateToken = genToken;
    }

    // oggetto utilizzabile nella mia applicazione Spring per fare l hash della password
    // da richiamare nel service per il controllo del login e criptazione della password
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:4200")); // Cambia con l'URL del tuo frontend
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/registration").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/get/{id_utente}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/user/edit/{id_utente}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/user/get/all").hasRole("ADMIN")
                        .requestMatchers("/user/handleStatus/{id_utente}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/user/upload/photo").hasRole("USER")
                        .requestMatchers("/cv/create").hasRole("USER")
                        .requestMatchers("/cv/get-all/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/cv/findByCompetenza").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/cv/findByEsperienze").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/cv/findByNome").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/cv/delete").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthFilter(this.generateToken), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
