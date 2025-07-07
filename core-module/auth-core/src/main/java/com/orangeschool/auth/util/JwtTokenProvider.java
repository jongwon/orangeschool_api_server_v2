package com.orangeschool.auth.util;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private String secretKey = "bdaslfb3285gjkdafbdf0513blbasdf513vc";

    private long accessTokenExpireSeconds = 1000 * 60 * 60 * 6; // 6시간
    private long passwordChangeTokenExpireSeconds = 1000 * 60 * 3; // 3분

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(Long id, String role) {
        try {
            Claims claims = Jwts.claims().setSubject("accessToken");
            claims.put("id", id);
            claims.put("role", role);
            Date now = new Date();
            return Jwts.builder().setClaims(claims).setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + accessTokenExpireSeconds))
                    .signWith(SignatureAlgorithm.HS256, secretKey).compact();
        } catch (Exception e) {
            return null;
        }
    }

    // public String createRefreshToken(String id, String role) {
    // try {
    // Claims claims = Jwts.claims().setSubject("refreshToken");
    // claims.put("id", id);
    // claims.put("role", role);
    // Date now = new Date();
    // return Jwts.builder().setClaims(claims).setIssuedAt(now)
    // .setExpiration(new Date(now.getTime() + refreshTokenExpireSeconds))
    // .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    // } catch (Exception e) {
    // return null;
    // }
    // }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            String id = claims.getBody().get("id").toString();
            String role = claims.getBody().get("role").toString();
            List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            roles.add(new SimpleGrantedAuthority(role));
            return new UsernamePasswordAuthenticationToken(id, "", roles);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.FORBIDDEN_INVALID_TOKEN);
        }
    }

    public Long getId(String token) throws Exception {
        try {
            Authentication authentication = getAuthentication(token);
            String principal = (String) authentication.getPrincipal();
            return Long.parseLong(principal);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.FORBIDDEN_INVALID_TOKEN);
        }
    }
}
