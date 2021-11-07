package br.com.ibb.digital.IBBcustomerOnboarding.controllers

import br.com.ibb.digital.IBBcustomerOnboarding.dtos.ErrorDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.sucessDTO
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import br.com.ibb.digital.IBBcustomerOnboarding.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/usuario")
class UserController (val userRepository: UserRepository){

   @PostMapping
   fun addUser(@RequestBody user : User): ResponseEntity<Any> {
        try{

            val errors = mutableListOf<String>()

            if(user == null){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Parâmetro de entrada não enviado", null), HttpStatus.BAD_REQUEST)
            }

            if(user.name.isNullOrBlank() || user.name.isNullOrEmpty()
                || user.name.length < 2){
                errors.add("Nome do usuário inválido")
            }

            if(user.email.isNullOrBlank() || user.email.isNullOrEmpty()
                || user.email.length < 5){
                errors.add("Email inválido")
            }

            if(user.password.isNullOrBlank() || user.password.isNullOrEmpty()
                || user.password.length < 4){
                errors.add("Senha inválida")
            }

            if(userRepository.findByEmail(user.email) != null){
                errors.add("Usuario já existe, tente fazer o login.")
            }
            
            if (errors.size > 0){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), null, errors), HttpStatus.BAD_REQUEST)
            }

            userRepository.save(user)
            return ResponseEntity(sucessDTO("Usuário cadastrado com sucesso"), HttpStatus.OK)
       }catch(exception : Exception) {
            return ResponseEntity(ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
               "não foi possivel efetuar o login, tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR)
       }
   }
}