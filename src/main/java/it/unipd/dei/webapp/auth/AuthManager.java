package it.unipd.dei.webapp.auth;

import it.unipd.dei.webapp.database.CreateTokenDatabase;
import it.unipd.dei.webapp.resource.Costumer;
import it.unipd.dei.webapp.resource.Token;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;

/**
 * Auth manager, class with some utilities to manage authentication
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class AuthManager {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    /**
     * The connection to the database
     */
    private final Connection con;


    public AuthManager(final Connection con){
        this.con = con;
    }

    public Token generateTokenForCostumer(Costumer c) throws SQLException {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String newToken = base64Encoder.encodeToString(randomBytes);
        long expireTime = System.currentTimeMillis() + 600000L; // 10 minutes from now

        Token tokenToInsert = new Token(
                newToken,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(expireTime),
                c.getId()
        );

        return new CreateTokenDatabase(con, tokenToInsert).insertToken();
    }

}
