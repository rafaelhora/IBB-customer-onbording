package br.com.ibb.digital.IBBcustomerOnboarding.controllers

import br.com.ibb.digital.IBBcustomerOnboarding.dtos.ErrorDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.LoginDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.loginResponseDTO
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import br.com.ibb.digital.IBBcustomerOnboarding.repositories.UserRepository
import br.com.ibb.digital.IBBcustomerOnboarding.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@RestController
@RequestMapping("/api/login")
class LoginController (val userRepository: UserRepository){

    @PostMapping
    fun checkLogin (@RequestBody dto: LoginDTO) : ResponseEntity<Any>{
        try{

            val user : User? = userRepository.findByCpfAndPassword(dto.cpf, dto.password)
            if(/*dto == null ||*/ isCPF(dto.cpf) || dto.password.isNullOrEmpty() || dto.password.isNullOrBlank()){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(),
                    "Entrada Inválida"), HttpStatus.BAD_REQUEST)
            }

            if (user != null) {
                if(dto.password != user.password){
                    return ResponseEntity(ErrorDTO(HttpStatus.FORBIDDEN.value(), "Senha incorreta, tente novamente"),HttpStatus.FORBIDDEN)
                }
            }

            val token = JWTUtils().createToken(user.toString(),)

            val tempUser = loginResponseDTO("Usuário Teste", dto.cpf, token)
            return ResponseEntity(tempUser, HttpStatus.OK)

        }catch(e: Exception){
            return ResponseEntity(ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possivel efetuar o login, tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    fun isCPF(document: String): Boolean {
        if (document.isEmpty()) return false

        val numbers = document.filter { it.isDigit() }.map {
            it.toString().toInt()
        }

        if (numbers.size != 11) return false

        //repeticao
        if (numbers.all { it == numbers[0] }) return false

        //digito 1
        val dv1 = ((0..8).sumOf { (it + 1) * numbers[it] }).rem(11).let {
            if (it >= 10) 0 else it
        }

        val dv2 = ((0..8).sumOf { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
            if (it >= 10) 0 else it
        }

        return numbers[9] == dv1 && numbers[10] == dv2
    }

}