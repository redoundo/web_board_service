package com.study.connection.config;

import com.study.connection.auth.*;
import com.study.connection.filter.JwtFilter;
import com.study.connection.filter.RedisCacheFilter;
import com.study.connection.filter.SessionFilter;
import com.study.connection.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * spring security configuration
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@ImportAutoConfiguration({SecurityFilterAutoConfiguration.class})
public class SecurityConfig {

    private final JwtAuthEntryPoint entryPoint;
    private final JwtAuthDeniedHandler deniedHandler;
    private final JwtProvider jwtProvider;
    private final SessionAuthenticationFailed sessionAuthFailed;
    private final CacheService cacheService;
    private final SecurityContextRepository repository = new HttpSessionSecurityContextRepository();
    @Value("${server.endpoint}")
    private String endpoint;
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    /**
     * 사용자 인증을 진행하는 url 로 접근시 실행되는 filterChain
     * @param security httpSecurity
     * @return SecurityFilterChain
     * @throws Exception exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain jwtChain(HttpSecurity security) throws Exception {
        security.securityMatcher("/api/auth/**", "/api/main/**", "/api/board/**", "/api/notify", "/api/question/**", "/api/gallery/**", "/api/user/**")
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling((exception)->
                        exception
                                .authenticationEntryPoint(entryPoint)
                                .accessDeniedHandler(deniedHandler)
                )
                .authorizeHttpRequests((authorizeHttpRequests) -> {
                    authorizeHttpRequests
                            .requestMatchers("/api/auth/login/**", "/api/auth/signIn/**" ).permitAll()
                            .requestMatchers("/api/main/**", "/api/board/view/download/**").permitAll()
                            .requestMatchers( "/api/notify/**", "/api/board/**",  "/api/question/**",  "/api/gallery/**").permitAll();
                    authorizeHttpRequests.requestMatchers("/api/user/**").hasRole("USER");
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtProvider, repository), UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    /**
     * 관리자 인증을 진행하는 url 로 접근시 실행되는 filterChain
     * @param security httpSecurity
     * @return SecurityFilterChain
     * @throws Exception exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain sessionChain(HttpSecurity security) throws Exception{
        security.securityMatcher("/admin/**", "/api/admin/**", "/auth/**")
                .securityContext(context -> context.requireExplicitSave(true))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> {
                    authorizeHttpRequests
                            .requestMatchers("/auth/login/**").permitAll();
                    authorizeHttpRequests
                            .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN");
                })
                .logout(logout -> {
                    logout.logoutUrl("/api/admin/logout")
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessUrl("/auth/login")
                    ;
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .invalidSessionUrl(endpoint + "/auth/login")
                                .sessionAuthenticationFailureHandler(sessionAuthFailed)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(true)
                                .expiredUrl(endpoint + "/auth/login"))
                .addFilterAfter(new RedisCacheFilter(repository, cacheService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SessionFilter(sessionRegistry(), repository) , UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }


    @Bean
    public WebSecurityCustomizer ignoreUrl() throws Exception {
        return (web) -> web.ignoring()
                .requestMatchers("/api/board/**", "/board/**", "/main/**", "/api/testing/**", "/js/**", "/css/**");
    }
}