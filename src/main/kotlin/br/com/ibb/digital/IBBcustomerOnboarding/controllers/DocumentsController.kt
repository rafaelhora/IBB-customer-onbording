package br.com.ibb.digital.IBBcustomerOnboarding.controllers

import br.com.ibb.digital.IBBcustomerOnboarding.dtos.ErrorDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.SucessDTO
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import br.com.ibb.digital.IBBcustomerOnboarding.repositories.DocsRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import br.com.ibb.digital.IBBcustomerOnboarding.models.Document
import org.springframework.web.bind.annotation.RequestBody
import kotlin.math.E

@RestController
@RequestMapping("/api/documentos")
class DocumentsController (val docsRepository : DocsRepository){
    @PostMapping("/selfie")
    fun getPhotos (@RequestBody document : Document) : ResponseEntity<Any>{
        val isValid: Boolean = true //Base64.isBase64(document.selfieBlob.toByteArray()) && Base64.isBase64(document.identificationBlob.toByteArray())
        try {
            if (!isValid) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Arquivo invalido, tente novamente"),
                    HttpStatus.BAD_REQUEST
                )
            }
            docsRepository.save(document)
            return ResponseEntity(SucessDTO("Fotos recebidas com sucesso"), HttpStatus.CREATED)
        }
        catch(e : Exception){
            return ResponseEntity(ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Nao foi possivel fazer o upload, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }
}