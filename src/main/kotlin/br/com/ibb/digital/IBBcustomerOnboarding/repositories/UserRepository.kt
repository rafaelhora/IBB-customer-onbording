package br.com.ibb.digital.IBBcustomerOnboarding.repositories

import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail (email : String) : User?
    fun findByCpf(cpf : String) : User?

}

