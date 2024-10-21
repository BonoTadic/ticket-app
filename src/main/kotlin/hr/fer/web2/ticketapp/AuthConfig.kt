package hr.fer.web2.ticketapp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Bean
    @Order(1)
    fun resourceServerFilter(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher(RequestHeaderRequestMatcher("Authorization"))
            .authorizeHttpRequests { requests ->
                requests.requestMatchers("/tickets").authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt {}
            }

        return http.build()
    }

    @Bean
    @Order(2)
    fun loginFilter(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/tickets/details/**").authenticated()
                    .requestMatchers("/tickets/total").permitAll()
            }
            .oauth2Login {}

        return http.build()
    }
}