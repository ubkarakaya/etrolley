package it.unipd.dei.webapp.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;


/**
 * Represents the data about a Token.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class Token extends Resource {
    private final String token;
    private final Timestamp creationTs;
    private final Timestamp expires;

    public String getToken() {
        return token;
    }

    public Timestamp getCreationTs() {
        return creationTs;
    }

    public Timestamp getExpires() {
        return expires;
    }

    public int getCostumerId() {
        return costumerId;
    }

    private final int costumerId;

    public Token(String token, Timestamp creationTs, Timestamp expires, int costumerId) {
        this.token = token;
        this.creationTs = creationTs;
        this.expires = expires;
        this.costumerId = costumerId;
    }

    @Override
    public void toJSON(OutputStream out) throws IOException {

    }
}
