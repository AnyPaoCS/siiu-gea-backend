package com.umss.siiu.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umss.siiu.core.util.ApplicationConstants;
import com.umss.siiu.core.util.DateGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class TokenService {

    @Value("${spring.secret.tokenKey}")
    private String secretKey;
    private ObjectMapper objectMapper;

    public TokenService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * This method generates a token compressing the information send by
     * parameter
     *
     * @param numberOfDays    Amount of time to keep the token active
     * @param objectToConvert
     * @return String Token generated in format JWT
     * @throws JsonProcessingException
     */
    public <T> String generateTokenByDay(int numberOfDays, T objectToConvert, boolean isBearerRequired)
            throws JsonProcessingException {
        String convertedObject = (objectToConvert instanceof String) ? (String) objectToConvert
                : objectMapper.writeValueAsString(objectToConvert);
        String compactJws = Jwts.builder().setSubject(convertedObject)
                .setExpiration(DateGenerator.addDaysToDate(numberOfDays)).signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return isBearerRequired ? ApplicationConstants.TOKEN_BEARER_PREFIX + compactJws : compactJws;
    }

    public void validateToken(String token) {
        Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token.replace(ApplicationConstants.TOKEN_BEARER_PREFIX, ""));
    }

    public <T> T getTokenInformation(String token, Class<T> classType) throws IOException {
        String jsonObject = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token.replace(ApplicationConstants.TOKEN_BEARER_PREFIX, "")).getBody().getSubject();
        return objectMapper.readValue(jsonObject, classType);
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(ApplicationConstants.HEADER_AUTHORIZACION_KEY);
        if (header != null && header.startsWith(ApplicationConstants.TOKEN_BEARER_PREFIX)) {
            return header;
        }
        String token = request.getParameter(ApplicationConstants.TOKEN);
        if (token != null && !token.equalsIgnoreCase(ApplicationConstants.UNDEFINED)) {
            return token;
        }
        return null;
    }

    public String getTokenInformationAsString(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token.replace(ApplicationConstants.TOKEN_BEARER_PREFIX, "")).getBody().getSubject();
    }

}
