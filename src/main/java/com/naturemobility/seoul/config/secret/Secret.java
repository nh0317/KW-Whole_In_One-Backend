package com.naturemobility.seoul.config.secret;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class Secret {

    public static SecretKey JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static String USER_INFO_PASSWORD_KEY = "o9pqYVC9-F8_.PEzEiw!L9F6.AYj9jcfVJ*_i.ifXYnyE68kix@Q2dL6rw*bV-rpdZYwcqZG-jPF-fw3CiJyKsfZ778ks-*jnZn";
//    public static String JWT_SECRET_KEY = "UwKYibQQgkW7g-*k.ap9kje-wxBHb9wdXoBT4vnt4P3sJWt-Nu";
}
