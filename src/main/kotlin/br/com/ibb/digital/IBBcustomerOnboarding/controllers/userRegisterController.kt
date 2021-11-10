package br.com.ibb.digital.IBBcustomerOnboarding.controllers

import br.com.ibb.digital.IBBcustomerOnboarding.dtos.ErrorDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.cpfDTO
import br.com.ibb.digital.IBBcustomerOnboarding.dtos.SucessDTO
import br.com.ibb.digital.IBBcustomerOnboarding.models.User
import br.com.ibb.digital.IBBcustomerOnboarding.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern


@RestController
@RequestMapping("/api/cadastro")
class UserRegisterController (val userRepository: UserRepository) {

    @GetMapping
    fun healthCheck() : ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/checkCpf")
    fun checkCpf (@RequestBody dto : cpfDTO) : ResponseEntity<Any> {
        if(dto.cpf == null || dto.cpf.isNullOrBlank() || dto.cpf.isNullOrEmpty()) {
            return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Insira o seu CPF"), HttpStatus.BAD_REQUEST)
        } else if(!isCPF(dto.cpf)){
            return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), "CPF Inválido"), HttpStatus.BAD_REQUEST)
        }
        //TODO: Checar em base global de dados
        val user: User? = userRepository.findByCpf(dto.cpf);
        if(user != null){
            return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Usuário encontrado"), HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(SucessDTO("Realizar cadastro"), HttpStatus.ACCEPTED)

    }

    @PostMapping("/addUser")
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
                || !isEmailValid(user.email)){
                errors.add("Email inválido")
            }

            if(user.password.isNullOrBlank() || user.password.isNullOrEmpty()
                || user.password.length < 4){
                errors.add("Senha inválida")
            }

            if(!isPhoneValid(user.phone)){
                errors.add("Telefone inválido")
            }

            val foundUser: User? = userRepository.findByEmail(user.email);
            if(foundUser != null){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Usuário encontrado"), HttpStatus.BAD_REQUEST)
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            if(isDateValid(LocalDate.parse(user.birthdate))) {
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Maior de 18 anos para abrir conta"), HttpStatus.BAD_REQUEST)
            }

            if (errors.size > 0){
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), null, errors), HttpStatus.BAD_REQUEST)
            }

            userRepository.save(user)
            return ResponseEntity(SucessDTO("Usuário cadastrado com sucesso"), HttpStatus.CREATED)
        }catch(exception : Exception) {
            return ResponseEntity(ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possivel efetuar o login, tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun isPhoneValid(phone : String) : Boolean {
        return true;
        //return Pattern.compile("(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})").matcher(phone).matches()
    }

    fun isDateValid(date : LocalDate) : Boolean {
        val now = LocalDate.now()

        val diff = ChronoUnit.YEARS.between(date, now);
        return diff > 18;
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

