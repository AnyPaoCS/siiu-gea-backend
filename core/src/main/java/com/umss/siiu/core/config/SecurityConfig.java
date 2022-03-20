package com.umss.siiu.core.config;

import com.umss.siiu.core.util.ApiPath;
import com.umss.siiu.core.util.ApplicationConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private JwtUnauthorizedHandler unauthorizedHandler;

    public SecurityConfig(UserDetailsService userDetailsService,
            JwtUnauthorizedHandler unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JwtAuthorizationFilter authorizationTokenFilterBean() {
        return new JwtAuthorizationFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and()
                .csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, ApiPath.LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .antMatchers(HttpMethod.POST, ApiPath.USER_PATH).permitAll()
                .antMatchers(HttpMethod.POST, ApiPath.FORGOTTEN_PASSWORD).permitAll()
                .antMatchers(HttpMethod.POST, ApiPath.RESTORE_PASSWORD).permitAll()
                .antMatchers(HttpMethod.GET, "/websocket/**").permitAll()
                .antMatchers(HttpMethod.GET, "/h2-console/*").permitAll()
                .antMatchers(HttpMethod.POST, "/h2-console/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/h2-console/*").permitAll()
                .antMatchers(HttpMethod.DELETE, "/h2-console/*").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/*").permitAll()

                .anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().addFilterBefore(authorizationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().frameOptions().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ApplicationConstants.ALLOWED_DOMAIN);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
