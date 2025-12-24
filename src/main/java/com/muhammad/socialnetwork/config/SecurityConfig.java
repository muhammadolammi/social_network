package com.muhammad.socialnetwork.config;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.muhammad.socialnetwork.filter.ClientAuthenticationFilter;
import com.muhammad.socialnetwork.filter.JWTFilter;
import com.muhammad.socialnetwork.model.User;
import com.muhammad.socialnetwork.repo.UserRepo;
import com.muhammad.socialnetwork.services.CustomOAuth2UserService;
import com.muhammad.socialnetwork.services.JWTService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity 
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;
        private final JWTService jwtService;
        private final UserRepo userRepo;
        private final CustomOAuth2UserService customOAuth2UserService ;
        private final ClientAuthenticationFilter clientAuthenticationFilter;

    // Spring looks for 'jwt.secret' in your .yml or imported .properties
    @Value("${env.frontend_url}")
    private String frontendUrl;




    public SecurityConfig(UserDetailsService userDetailsService, JWTFilter jwtFilter,JWTService jwtService, UserRepo userRepo, CustomOAuth2UserService customOAuth2UserService, ClientAuthenticationFilter clientAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter=jwtFilter;
        this.jwtService = jwtService;
        this.userRepo=userRepo;
        this.customOAuth2UserService= customOAuth2UserService;
        this.clientAuthenticationFilter=clientAuthenticationFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // Usually disabled for JWT
            // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login").permitAll()
                .anyRequest().authenticated()
            )
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        // .oauth2Login(oauth2 -> oauth2
        //     .defaultSuccessUrl("/api/dashboard", true)
        // );
        //    .oauth2Login(Customizer.withDefaults());
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
        .userService(customOAuth2UserService) // Inject your service here
             )
            .successHandler((request, response, authentication) -> {
                handleOAuth2Success(request, response, authentication);
       
            })
        )
        .addFilterBefore(clientAuthenticationFilter, JWTFilter.class)
        ;
        return http.build();
    }

    private void handleOAuth2Success(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
          // Cast to OAuth2User to see all the GitHub data
        // OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // 1. Identify the Provider
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String provider = authToken.getAuthorizedClientRegistrationId(); // "google" or "github"
        String email = authentication.getName();
       String cleanUsername = email.contains("@") ? email.split("@")[0] : email;
       User currentUser;
        // get user with email
        User user = userRepo.findByEmail(email);
        if (user==null){
            // create user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(cleanUsername);
                    // 4. Update the specific link flag based on the provider
            if ("github".equals(provider)) {
                newUser.setLinkedWithGithub(true);
            } else if ("google".equals(provider)) {
                newUser.setLinkedWithGoogle(true);
            }
            currentUser=newUser;
            User _ = userRepo.save(newUser);
        }else{
             if ("github".equals(provider)) {
                user.setLinkedWithGithub(true);
            } else if ("google".equals(provider)) {
                user.setLinkedWithGoogle(true);
            }
            currentUser=user;
            User _ = userRepo.save(user);
        }

        // // 2. Generate your JWT
        String token = jwtService.generateToken(currentUser.getUsername());
        // // // 3. Send the user back to your Frontend (e.g. React)
        response.sendRedirect( frontendUrl + "/oauth2-success?token=" + token);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // config.setAllowedOrigins(List.of("http://localhost:3000")); // Better than "*"
                config.setAllowedOrigins(List.of("*")); 
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(12);
}

@Bean
public AuthenticationManager authenticationManager (AuthenticationConfiguration config){
    return config.getAuthenticationManager();

}
}