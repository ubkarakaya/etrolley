package it.unipd.dei.webapp.resource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the data about login.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class CostumerLogin extends Resource {
    private final String email;
    private final String password;
    private final boolean remember;


    public CostumerLogin(String email, String password, boolean remember) {
        this.email = email;
        this.password = password;
        this.remember = remember;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRemember() {
        return remember;
    }

    /**
     * Creates a {@code CostumerLogin} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     *
     * @return the {@code CostumerLogin} created from the JSON representation.
     *
     * @throws IOException if something goes wrong while parsing.
     */
    public static CostumerLogin fromJSON(final InputStream in) throws IOException {
        String email = "";
        String password = "";
        boolean remember = false;

        final JsonParser jp = JSON_FACTORY.createParser(in);

        // while we are not on the start of an element or the element is not
        // a token element, advance to the next element (if any)
        while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "costumerLogin".equals(jp.getCurrentName()) == false) {

            // there are no more events
            if (jp.nextToken() == null) {
                throw new IOException("Unable to parse JSON: no costumerLogin object found.");
            }
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {

            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                switch (jp.getCurrentName()) {
                    case "email":
                        jp.nextToken();
                        email = jp.getText();
                        break;
                    case "password":
                        jp.nextToken();
                        password = jp.getText();
                        break;
                    case "remember":
                        jp.nextToken();
                        remember = jp.getBooleanValue();
                        break;
                }
            }
        }

        return new CostumerLogin(email, password, remember);
    }

    @Override
    public void toJSON(OutputStream out) throws IOException {

    }
}
