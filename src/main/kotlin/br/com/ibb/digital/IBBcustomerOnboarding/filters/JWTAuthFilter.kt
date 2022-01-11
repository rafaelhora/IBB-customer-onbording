package br.com.ibb.digital.IBBcustomerOnboarding.filters

import br.com.ibb.digital.IBBcustomerOnboarding.authorization
import br.com.ibb.digital.IBBcustomerOnboarding.bearer
import br.com.ibb.digital.IBBcustomerOnboarding.impl.UserDetailImpl
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import br.com.ibb.digital.IBBcustomerOnboarding.repositories.UserRepository
import br.com.ibb.digital.IBBcustomerOnboarding.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthFilter(authenticationManager: AuthenticationManager, val jwtUtils: JWTUtils, val userRepository: UserRepository)
    : BasicAuthenticationFilter(authenticationManager){

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)

        if(authorizationHeader != null && authorizationHeader.startsWith(bearer)){
            val approved = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = approved

        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorization: String): UsernamePasswordAuthenticationToken{
        val token = authorization.substring(7)
        if(jwtUtils.isTokenValid(token)) {
            val idString = jwtUtils.getUserId(token)
            if(!idString.isNullOrEmpty() && !idString.isNullOrBlank()){
                val user = userRepository.findByIdOrNull(idString.toLong()) ?: throw UsernameNotFoundException("Usuário não encontrado")
                val userImpl = UserDetailImpl(user)
                return UsernamePasswordAuthenticationToken(userImpl,null, userImpl.authorities)
            }
        }

        throw UsernameNotFoundException("Token não é válido, ou o usuário não foi encontrado")
    }
}