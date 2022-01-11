package br.com.ibb.digital.IBBcustomerOnboarding.configurations

import br.com.ibb.digital.IBBcustomerOnboarding.filters.JWTAuthFilter
import br.com.ibb.digital.IBBcustomerOnboarding.repositories.UserRepository
import br.com.ibb.digital.IBBcustomerOnboarding.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Autowired
    private lateinit var userRepository : UserRepository

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()
            .antMatchers(HttpMethod.POST, "/api/cadastro").permitAll()
            .antMatchers(HttpMethod.GET, "/api/cadastro").permitAll()
            .antMatchers(HttpMethod.POST, "/api/cadastro/search-user").permitAll()
            .antMatchers(HttpMethod.POST, "/api/cadastro/add-user").permitAll()
            .antMatchers(HttpMethod.POST, "/api/documentos/selfie").permitAll()
            .anyRequest().authenticated()

        http.cors().configurationSource(configCors())
        http.addFilter(JWTAuthFilter(authenticationManager(), jwtUtils, userRepository))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun configCors() : CorsConfigurationSource? {
        val config = CorsConfiguration()

        config.allowedOrigins = mutableListOf("*")
        config.allowedMethods = mutableListOf("*")
        config.allowedHeaders = mutableListOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}