package br.com.ibb.digital.IBBcustomerOnboarding.impl

import org.springframework.security.core.userdetails.UserDetails
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import org.springframework.security.core.GrantedAuthority

class UserDetailImpl(private val user : User) : UserDetails {
    override fun getAuthorities() = mutableListOf<GrantedAuthority>()

    override fun getPassword() = user.password

    override fun getUsername() = user.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked()  = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}