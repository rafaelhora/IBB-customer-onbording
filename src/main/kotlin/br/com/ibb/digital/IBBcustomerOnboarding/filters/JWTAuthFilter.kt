package br.com.ibb.digital.IBBcustomerOnboarding.filters

import br.com.ibb.digital.IBBcustomerOnboarding.authorization
import br.com.ibb.digital.IBBcustomerOnboarding.bearer
import br.com.ibb.digital.IBBcustomerOnboarding.impl.UserDetailImpl
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import br.com.ibb.digital.IBBcustomerOnboarding.utils.JWTUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthFilter(authenticationManager: AuthenticationManager, val jwtUtils: JWTUtils)
    : BasicAuthenticationFilter(authenticationManager){

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorization = request.getHeader(authorization)
        if(authorization != null && authorization.startsWith(bearer)){
            val approved = getAuthentication(authorization)
            SecurityContextHolder.getContext().authentication = approved

        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorization: String): UsernamePasswordAuthenticationToken{
        val token = authorization.substring(7)
        if(jwtUtils.isTokenValid(token)) {
            val idString = jwtUtils.getUserId(token)
            if(!idString.isNullOrEmpty() && !idString.isNullOrBlank()){
                val user = User(idString.toLong(), "Usuario Teste", "admin@admin.com", "admin1234")
                val userImpl = UserDetailImpl(user)
                return UsernamePasswordAuthenticationToken(userImpl,null, userImpl.authorities)
            }
        }

        throw UsernameNotFoundException("Token não é válido, ou o usuário não foi encontrado")
    }
}