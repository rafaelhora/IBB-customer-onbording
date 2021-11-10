package br.com.ibb.digital.IBBcustomerOnboarding.configurations

import br.com.ibb.digital.IBBcustomerOnboarding.filters.JWTAuthFilter
import br.com.ibb.digital.IBBcustomerOnboarding.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/cadastro/checkCpf").permitAll()
            .antMatchers(HttpMethod.POST, "/api/cadastro/addUser").permitAll()
            .antMatchers(HttpMethod.GET, "/api/cadastro").permitAll()
            .antMatchers(HttpMethod.POST, "/api/documentos/selfie").permitAll()
            .anyRequest().authenticated()
        http.addFilter(JWTAuthFilter(authenticationManager(), jwtUtils))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}