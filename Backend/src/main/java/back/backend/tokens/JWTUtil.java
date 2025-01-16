package back.backend.tokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXP_TIME = 864_000_000;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact().replace(" ", "");
    }
}