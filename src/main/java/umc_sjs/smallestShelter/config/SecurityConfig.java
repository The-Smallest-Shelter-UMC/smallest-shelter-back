
package umc_sjs.smallestShelter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;
import umc_sjs.smallestShelter.config.jwt.*;
import umc_sjs.smallestShelter.user.UserRepository;

//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilter())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests(authroize -> authroize.antMatchers("/auth/organization/**")
                        .access("hasRole('ROLE_ORGANIZATION')")
                        .antMatchers("/auth/private/**")
                        .access("hasRole('ROLE_PRIVATE')")
                        .antMatchers("/auth/**")
                        .access("hasRole('ROLE_PRIVATE') or hasRole('ROLE_ORGANIZATION')")
                        .anyRequest().permitAll())
                //.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler).and()
                .build();
    }

    public class CustomFilter extends AbstractHttpConfigurer<CustomFilter, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
                    .addFilterBefore(new JwtExceptionFilter(), JwtAuthorizationFilter.class);
        }
    }
}

