package com.hzf.auth.security;

import com.alibaba.fastjson2.JSON;
import com.hzf.auth.common.ResponseEntity;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * @author : zy
 * JWT工具类
 */
public class JWTUtil {

    private static final String secret = "xpo1xgnl5ksinxkgu1nb6vcx3zaq1wsxvv";

    private static final long EXPIRE = 1000 * 60 * 60 * 12;

    public static String createToken(Claims claims) {
        try {
            JWSSigner jwsSigner = new MACSigner(secret);
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
            claims.setJti(UUID.randomUUID().toString());
            claims.setIat(new Date().getTime());
            claims.setExp(new Date(System.currentTimeMillis() + EXPIRE).getTime());
            Payload payload = new Payload(JSON.toJSONString(claims));
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证并获取用户信息
     *
     * @param token 令牌
     * @return 解析后用户信息
     */
    public static ResponseEntity<Claims> verifyToken(String token) {
        JWSObject jwsObject;
        ResponseEntity<Claims> response = new ResponseEntity<>();
        try {
            jwsObject = JWSObject.parse(token);
            JWSVerifier jwsVerifier = new MACVerifier(secret);
            if (!jwsObject.verify(jwsVerifier)) {
                response.setCode(10008).setErrorMsg("token无效");
                return response;
            }
            String payload = jwsObject.getPayload().toString();
            Claims claims = JSON.parseObject(payload, Claims.class);
            if (claims.getExp() < new Date().getTime()) {
                response.setCode(10008).setErrorMsg("token无效");
                return response;
            }
            response.setCode(200).setData(claims).setMessage("解析成功");
            return response;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
        }
        response.setCode(10008).setErrorMsg("token无效");
        return response;
    }
}