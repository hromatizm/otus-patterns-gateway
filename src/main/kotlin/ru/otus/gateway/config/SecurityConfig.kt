package ru.otus.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import spring.jwt.JwtAuthenticationFilter
import spring.jwt.JwtPublicKeyHolder

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtPublicKeyHolder: JwtPublicKeyHolder) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/api/v1/register", "/api/v1/login").permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val jwtDecoder = NimbusJwtDecoder.withPublicKey(jwtPublicKeyHolder.key).build()
        return JwtAuthenticationFilter(jwtDecoder)
    }
}