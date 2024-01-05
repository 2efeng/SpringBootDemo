package com.hzf.auth.config;

import com.hzf.auth.models.system.Api;
import com.hzf.auth.models.system.Role;
import com.hzf.auth.models.system.User;
import com.hzf.auth.repository.system.ApiRepository;
import com.hzf.auth.security.JwtAuthFilter;
import com.hzf.auth.security.LoginUser;
import com.hzf.auth.service.system.UserService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Autowired
    ApiRepository apiRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@Nonnull InterceptorRegistry registry) {
                registry.addInterceptor(new RequestLoggingAspect()).addPathPatterns("/api/**");
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.selectByName(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            byte status = user.getStatus();
            if (status != 0) {
                throw new LockedException("用户已停用");
            }
            return new LoginUser(user);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<Api> apis = apiRepository.findAll();
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorize) -> {
            for (Api api :apis){
                for (Role role : api.getRoles()){
                    authorize.requestMatchers(api.getPath()).hasRole(role.getRoleName());
                }
            }
            authorize.anyRequest().authenticated();
        });
        http.authenticationManager(authenticationManager());
        http.addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/auth/login").requestMatchers("/api/auth/logout").requestMatchers("/swagger-ui/**").requestMatchers("/swagger-ui.html").requestMatchers("/swagger-ui").requestMatchers("/doc.html").requestMatchers("/swagger-resources").requestMatchers("/v3/api-docs/**").requestMatchers("/favicon.ico").requestMatchers("/error");
    }


}
