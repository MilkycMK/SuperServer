package net.mlk.adolfserver.security;

import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.user.session.Session;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class Token {
    private static final int MASK_LENGTH = 4;

    private String userName;
    private String token;
    private final String mask;

    private boolean expiring;
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;

    public Token(String token, String mask) {
        this.mask = mask;
        this.token = token;
        this.decodeToken(token);
    }

    public Token(String userName, int userId, boolean expiring, long seconds) {
        this.userName = userName;
        this.mask = this.generateMask();
        this.expiring = expiring;
        if (expiring) {
            this.creationTime = LocalDateTime.now();
            this.expirationTime = this.creationTime.plusSeconds(seconds);
        }
        this.token = encodeToken(userName);
        Session session = new Session(userId, this);
    }

    private String encodeToken(String name) {
        StringBuilder token = new StringBuilder(name + "@");

        String tokenBase = this.generateString();
        if (this.expiring) {
            token.append("expiring=true@")
                    .append(this.creationTime.format(AdolfServerApplication.TIMEDATE_FORMAT)).append("@")
                    .append(this.expirationTime.format(AdolfServerApplication.TIMEDATE_FORMAT)).append("@");
        } else {
            token.append("@expiring=false@");
        }

        token.append(tokenBase);
        token = new StringBuilder(this.maskString(token.toString()));
        return token.toString();
    }

    public void decodeToken(String token) /* throws InvalidTokenException */ {
        token = unmaskString(token);
        String[] tokenParts = token.split("@");
        this.userName = tokenParts[0];
        this.expiring = Boolean.parseBoolean(tokenParts[1].split("=")[1]);

        if (this.expiring) {
            this.creationTime = LocalDateTime.parse(tokenParts[2], AdolfServerApplication.TIMEDATE_FORMAT);
            this.expirationTime = LocalDateTime.parse(tokenParts[3], AdolfServerApplication.TIMEDATE_FORMAT);
        }
    }

    private String generateMask() {
        Random random = new Random();
        StringBuilder mask = new StringBuilder();
        for (int i = 0; i < MASK_LENGTH; i++) {
            mask.append(random.nextInt(10));
        }
        return mask.toString();
    }

    private String generateString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String maskString(String string) {
        return maskString(string, this.mask);
    }

    private static String maskString(String string, String mask) {
        byte[] byteString = string.getBytes(StandardCharsets.UTF_8);
        return java.util.Base64.getEncoder().encodeToString(applyMask(byteString, mask));
    }

    private String unmaskString(String string) {
        return unmaskString(string, this.mask);
    }

    private static String unmaskString(String string, String mask) {
        byte[] maskedBytes = java.util.Base64.getDecoder().decode(string);
        return new String(applyMask(maskedBytes, mask), StandardCharsets.UTF_8);
    }

    private static byte[] applyMask(byte[] byteString, String mask) {
        byte[] byteMask = mask.getBytes(StandardCharsets.UTF_8);

        byte[] resultBytes = new byte[byteString.length];
        for (int i = 0; i < byteString.length; i++) {
            resultBytes[i] = (byte) (byteString[i] ^ byteMask[i % byteMask.length]);
        }
        return resultBytes;
    }

    public boolean isExpiring() {
        return this.expiring;
    }

    public String getToken() {
        return this.token;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public LocalDateTime getExpirationTime() {
        return this.expirationTime;
    }

    public boolean isValid() {
        return this.expirationTime.isBefore(LocalDateTime.now());
    }

    public String getMask() {
        return this.mask;
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public String toString() {
        return this.token;
    }

}
