package br.com.ibb.digital.IBBcustomerOnboarding.repositories

import br.com.ibb.digital.IBBcustomerOnboarding.models.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocsRepository : JpaRepository<Document, Long> {

}