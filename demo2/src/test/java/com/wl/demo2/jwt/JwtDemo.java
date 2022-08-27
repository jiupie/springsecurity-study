package com.wl.demo2.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;

@SpringBootTest
public class JwtDemo {
    @Test
    public void jwtHS() {
        byte[] keyBytes = Decoders.BASE64.decode("sdfdsahsajdffasdafsdhsdjfdjfkjasdfjkssfdjhsalkajfsdfasadfdsa");
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String compact = Jwts.builder().signWith(key, SignatureAlgorithm.HS256)
                .setId("11")
                .claim("user", "sdsafdfsdafasd")
                .setSubject("南顾北衫")
                .compact();
        System.out.println(compact);
    }
    @Test
    public void  jwtParse(){
        Claims body = Jwts.parserBuilder().setSigningKey("sdfdsahsajdffasdafsdhsdjfdjfkjasdfjkssfdjhsalkajfsdfasadfdsa")
                .build().parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMSIsInVzZXIiOiJzZHNhZmRmc2RhZmFzZCIsInN1YiI6IuWNl-mhvuWMl-ihqyJ9.6SInSrd6zh9iT3JaVLSaiZUZ2sG516RzNoyH2iMc1Ig")
                .getBody();

        System.out.println(body.getSubject());

//        System.out.println(Jwts.parserBuilder().setSigningKey("sdfdsahsajdffasdafsdhsdjfdjfkjasdfjkssfdjhsalkajfsdfasadfdsa")
//                .build().parsePlaintextJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMSIsInVzZXIiOiJzZHNhZmRmc2RhZmFzZCIsInN1YiI6IuWNl-mhvuWMl-ihqyJ9.6SInSrd6zh9iT3JaVLSaiZUZ2sG516RzNoyH2iMc1Ig")
//                .getBody());

        System.out.println(Jwts.parserBuilder().setSigningKey("sdfdsahsajdffasdafsdhsdjfdjfkjasdfjkssfdjhsalkajfsdfasadfdsa")
                .build().parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMSIsInVzZXIiOiJzZHNhZmRmc2RhZmFzZCIsInN1YiI6IuWNl-mhvuWMl-ihqyJ9.6SInSrd6zh9iT3JaVLSaiZUZ2sG516RzNoyH2iMc1Ig")
                .getBody().get("user"));
    }


}
