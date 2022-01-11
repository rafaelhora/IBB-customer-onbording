package br.com.ibb.digital.IBBcustomerOnboarding.models

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                val id:Long = 0,
    val name: String = "",
    val email: String = "",
    var password: String = "",
    val cpf: String = "",
    val address: String ="",
    val phone: String = "",
    val birthdate: String = "")