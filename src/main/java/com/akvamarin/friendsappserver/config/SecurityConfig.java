package com.akvamarin.friendsappserver.config;

import com.akvamarin.friendsappserver.security.jwt.JwtAuthenticationEntryPoint;
import com.akvamarin.friendsappserver.security.jwt.JwtTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/**";
    private static final String[] SWAGGER_URLS = {
            "/v2/api-docs",             // Swagger UI v2
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",          // Swagger UI v3
            "/rest-api-docs/**",
            "/swagger-ui/**"
    };

    private final JwtTokenAuthenticationFilter authenticationFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity = httpSecurity
                .cors() // Access-Control-Allow-Origin (для браузеров)
                .and()
                .csrf().disable() // CSRF-токен
                .headers().frameOptions().disable() //X-Frame-Options выкл.
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //сессии не создавать
                .and()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(unauthorizedHandler));

        httpSecurity.authorizeRequests()//предоставить доступ:
                .antMatchers("/api/v1/auth/**").permitAll() //для всех
                .antMatchers("/api/v1/files/**").permitAll()
                .antMatchers("/api/v1/users/**").permitAll()
                .antMatchers("/api/v1/events/**").permitAll()
                .antMatchers("/api/v1/cities/**").permitAll()
                .antMatchers("/api/v1/categories/**").permitAll()
                .antMatchers("/api/v1/images/**").permitAll()
                .antMatchers("/api/v1/comments/**").permitAll()
                .antMatchers("/api/v1/notifications/**").permitAll()
                .antMatchers("/images/**").permitAll() // ресурс картинок
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN") //только для админа
                .antMatchers(LOGIN_ENDPOINT).permitAll() //для всех
                .antMatchers(SWAGGER_URLS).permitAll() //для всех
                .anyRequest().authenticated(); //для других запросов требется аунтентификация

        //добавление созданного JWT-фильтра Spring Security
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
