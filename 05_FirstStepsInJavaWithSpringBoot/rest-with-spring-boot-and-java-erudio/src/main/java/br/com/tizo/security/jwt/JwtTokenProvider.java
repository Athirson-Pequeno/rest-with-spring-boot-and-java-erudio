package br.com.tizo.security.jwt;

import br.com.tizo.data.vo.v1.security.TokenVO;
import br.com.tizo.exceptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validateInMilliseconds = 3600000; // 1h

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO refreshToken(String refreshToken) {

        if (refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring("Bearer ".length());

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validate = new Date(now.getTime() + validateInMilliseconds);
        var accessToken = getAccessToken(username, roles, now, validate);
        var refreshToken = getRefreshToken(username, roles, now);
        return new TokenVO(username, true, now, validate, accessToken, refreshToken);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {

        Date validateRefreshToken = new Date(now.getTime() + (validateInMilliseconds * 3));

        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validateRefreshToken)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();

    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validate) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validate)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
        
    }

    private DecodedJWT decodedToken(String token) {

        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier jwtVerifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        return decodedJWT;

    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null){
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }

    public Boolean validateToken(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if(decodedJWT.getExpiresAt().before(new Date())){
                return false;
            }
            return true;
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
