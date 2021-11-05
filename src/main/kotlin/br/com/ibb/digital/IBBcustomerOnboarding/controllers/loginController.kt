package br.com.ibb.digital.IBBcustomerOnboarding.controllers

import br.com.ibb.digital.IBBcustomerOnboarding.dtos.ErrorDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.LoginDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@RestController
@RequestMapping("api/login")
class loginController {

    @PostMapping
    fun checkLogin (@RequestBody dto: LoginDTO) : ResponseEntity<Any>{
        try{
            if(/*dto == null ||*/ dto.login.isNullOrBlank() || dto.login.isNullOrEmpty()
                || dto.password.isNullOrEmpty() || dto.password.isNullOrBlank()){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(),
                    "Entrada Inválida"), HttpStatus.BAD_REQUEST)
            }

            return ResponseEntity("Login autenticado com sucesso!", HttpStatus.OK)
        }catch(e: Exception){
            return ResponseEntity(ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "não foi possivel efetuar o login, tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

}