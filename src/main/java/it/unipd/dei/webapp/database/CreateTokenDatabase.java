package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.CostumerLogin;
import it.unipd.dei.webapp.resource.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a token.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public final class CreateTokenDatabase {
    private static final String STATEMENT = "INSERT INTO tokens VALUES (?,?,?,?) RETURNING *";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The token
     */
    private final Token token;

    /**
     * Creates a new object
     *
     * @param con
     *            the connection to the database.
     * @param token
     *
     */
    public CreateTokenDatabase(final Connection con, final Token token) {
        this.con = con;
        this.token = token;
    }

    public Token insertToken() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the create token
        Token t = null;

        try {
            // token, creation_ts, expires, customer_id
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, token.getToken());
            pstmt.setTimestamp(2, token.getCreationTs());
            pstmt.setTimestamp(3, token.getExpires());
            pstmt.setInt(4, token.getCostumerId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // token, creation_ts, expires, customer_id
                t = new Token(
                        rs.getString("token"),
                        rs.getTimestamp("creation_ts"),
                        rs.getTimestamp("expires"),
                        rs.getInt("costumer_id")
                );
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            con.close();
        }

        return t;
    }

}
