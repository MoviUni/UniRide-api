package com.example.unirideapi.security;

import com.example.unirideapi.model.Usuario;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.example.unirideapi.exception.RoleNotFoundException;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.validity-in-seconds}")
    private long jwtValidityInSeconds;

    private Key key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        // Generar la clave para firmar el JWT a partir del secreto configurado
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        // Inicializar el parser JWT con la clave generada para firmar y validar tokens
        jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build();
    }

    //TODO: Metodo para crear el token JWT con los detalles del usuario autenticado
    public String createAccessToken(Authentication authentication) throws RoleNotFoundException {
        // TODO: Obtener el email o nombre del usuario autenticado
        String email = authentication.getName();

        // TODO: Obtener el rol de usuario desde el objeto de autenticación
        String role = authentication
                .getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(RoleNotFoundException::new)
                .getAuthority();

        //TODO: Comstruir y firmar el token JWT que incluye el rol y el email
        return Jwts
                .builder()
                .setSubject(email)  //El sujeto del token es el email o nombre del usuario
                .claim("role", role)    //El rol se incluye como claim en el token
                .signWith(key, SignatureAlgorithm.HS512)    //Firmar el token con el algoritmo HS512 y la clave generada
                .setExpiration(new Date(System.currentTimeMillis() + jwtValidityInSeconds * 1000))
                .compact();
    }

    //TODO: Metodo para obtener la autenticacion a partir del token JWT
    public Authentication getAuthentication(String token) {
        //TODO: Extraer los claims (datos) del token JWT
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        //TODO: Obtener el rol del token
        String role = claims.get("role").toString();

        //TODO: Crear la lista de autoridades(roles) para el usuario
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        //TODO: El principal del contexto de seguridad es el email (subject) extraido del token
        User principal = new User(claims.getSubject(), "", authorities);

        //TODO: Crear el objeto de autenticación con los detalles del usuario
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    //TODO: Metodo para validar el token JWT(si está firmado correctamente y no ha expirado)
    public boolean validateToken(String token) {
        try {
            //TODO: Parsear el token para verificar su validez
            jwtParser.parseClaimsJws(token);
            return true; //Si no lanza excepción, el token es válido
        } catch (JwtException e) {
            return false; //Si hay algún error al parsear, el token no es válido
        }
    }

    public String createAccessTokenFromUsuario(Usuario usuario) throws RoleNotFoundException {
        String email = usuario.getEmail();

        if (usuario.getRol() == null || usuario.getRol().getName() == null) {
            throw new RoleNotFoundException("El usuario no tiene rol asignado");
        }

        String role = usuario.getRol().getName().name(); // ej: "PACIENTE" o "PSICOLOGO"

        return Jwts
                .builder()
                .setSubject(email)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + jwtValidityInSeconds * 1000))
                .compact();
    }

}
