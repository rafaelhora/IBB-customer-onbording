package br.com.ibb.digital.IBBcustomerOnboarding.controllers

import br.com.ibb.digital.IBBcustomerOnboarding.dtos.ErrorDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.LoginDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.loginResponseDTO
import br.com.ibb.digital.IBBcustomerOnboarding.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@RestController
@RequestMapping("api/login")
class LoginController {
    private val LOGIN_TESTE = "admin@admin.com"
    private val SENHA_TESTE = "admin1234"
    @PostMapping
    fun checkLogin (@RequestBody dto: LoginDTO) : ResponseEntity<Any>{
        try{
            if(/*dto == null ||*/ dto.login.isNullOrBlank() || dto.login.isNullOrEmpty()
                || dto.password.isNullOrEmpty() || dto.password.isNullOrBlank()
                || dto.login != LOGIN_TESTE || dto.password != SENHA_TESTE){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(),
                    "Entrada Inválida"), HttpStatus.BAD_REQUEST)
            }

            val usuario = 1
            val token = JWTUtils().createToken(usuario.toString(),)

            val tempUser = loginResponseDTO("Usuário Teste", LOGIN_TESTE, token)
            return ResponseEntity(tempUser, HttpStatus.OK)

        }catch(e: Exception){
            return ResponseEntity(ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "não foi possivel efetuar o login, tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

}