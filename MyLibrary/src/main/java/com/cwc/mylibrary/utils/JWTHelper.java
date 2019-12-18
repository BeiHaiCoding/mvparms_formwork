package com.cwc.mylibrary.utils;


import android.support.annotation.NonNull;

import com.cwc.mylibrary.Log.MLogHelper;
import com.cwc.mylibrary.model.ResultModel;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 *
 */
public class JWTHelper {
    static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    static String base64EncodedSecretKey = "25b991321a2c677e2e16629d185e4501";// jwt秘钥
    static String tokenEncodedSecretKey = "cywkj@#";// token解密秘钥

    public static String encodeJwt(Map<String, Object> jwtBodyMap) {
        String compactJws = Jwts.builder()
                .setClaims(jwtBodyMap)
                .setHeaderParam("typ", "JWT")
                .signWith(signatureAlgorithm, base64EncodedSecretKey.getBytes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))
                .compact();

        return compactJws;
    }

    public static String parseJwt(String jwtBody) {

        try {

            Jws<Claims> jws = Jwts.parser().setSigningKey(base64EncodedSecretKey.getBytes()).parseClaimsJws(jwtBody);
            Claims claims = jws.getBody();

            Gson gson = new Gson();
            return gson.toJson(claims);
            //OK, we can trust this JWT

        } catch (SignatureException e) {
            MLogHelper.i("SignatureException", e.getMessage());
            //don't trust the JWT!
            return "SignatureException";

        }
    }

    public static String parseJwtToken(String jwtBody) {

        try {

            Jws<Claims> jws = Jwts.parser().setSigningKey(tokenEncodedSecretKey.getBytes()).parseClaimsJws(jwtBody);
            Claims claims = jws.getBody();

            Gson gson = new Gson();
            return gson.toJson(claims);
            //OK, we can trust this JWT

        } catch (SignatureException e) {
            MLogHelper.i("SignatureException", e.getMessage());
            //don't trust the JWT!
            return "SignatureException";

        }
    }

}
