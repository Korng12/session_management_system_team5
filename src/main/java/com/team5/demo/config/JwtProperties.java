package com.team5.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret ; // Use a strong key (at least 256 bits)
    private long exp; // 1 hour

    // getters/setters
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public long getExp() {
        return exp;
    }
    public void setExp(long exp) {
        this.exp = exp;
    }
}
