package br.com.ibb.digital.IBBcustomerOnboarding.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JWTUtils {

    private val secretKey = "toboldlygowherenoonehasgonebefore"

    fun createToken(userId : String) : String {
        return Jwts.builder()
            .setSubject(userId)
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray())
            .compact()
    }

    fun isTokenValid (token : String) : Boolean {
        val claims = getClaimsToken(token)
        if(claims != null){
            val userId = claims.subject
            if (!userId.isNullOrEmpty() && !userId.isNullOrBlank())
                return true
        }

        return false

    }

    fun getClaimsToken(token : String) : Claims? {
        return try {
            Jwts.parser().setSigningKey(secretKey.toByteArray()).parseClaimsJws(token).body
        } catch(exception : Exception) {
            null
        }
    }

    fun getUserId(token : String) : String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }
}