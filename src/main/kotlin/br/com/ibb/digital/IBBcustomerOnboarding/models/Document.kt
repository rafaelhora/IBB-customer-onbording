package br.com.ibb.digital.IBBcustomerOnboarding.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Document (

    @Id @GeneratedValue
    val id : Long = 0,
    val userId : Long,
    val selfieBlob : String,
    val identificationBlob : String

)