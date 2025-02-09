package hr.fer.web2.ticketapp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Bean
    @Order(1)
    fun apiSecurityConfig(http: HttpSecurity) : SecurityFilterChain {
        return http
            .securityMatcher(RequestHeaderRequestMatcher("Authorization"))
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/tickets/create").authenticated()
            }
            .oauth2ResourceServer {
                it.jwt { }
            }
            .build()
    }

    @Bean
    @Order(2)
    fun userLoginSecurityConfig(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/tickets/details/**").authenticated()
                it.requestMatchers(HttpMethod.GET, "/tickets/total").permitAll()
            }
            .oauth2Login { }
            .build()
    }
}