package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.Costumer;
import it.unipd.dei.webapp.resource.CostumerLogin;
import it.unipd.dei.webapp.resource.Token;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Customer Login
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public final class CostumerLoginDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "SELECT * FROM costumer WHERE email = ?";

    private static final String STATEMENT_TOKEN = "INSERT INTO tokens VALUES (?,?,?,?)";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The costumer t
     */
    private final CostumerLogin costumer;

    /**
     * Creates a new object
     *
     * @param con
     *            the connection to the database.
     * @param costumer
     *            the costumer that tries to login
     */
    public CostumerLoginDatabase(final Connection con, final CostumerLogin costumer) {
        this.con = con;
        this.costumer = costumer;
    }

    /**
     * Gets a costumer by email in order then to perform the login
     *
     * @return the {@code Costumer} object matching the id.
     *
     * @throws SQLException
     *             if any error occurs while reading the customer.
     */
    public Costumer getCostumerForLogin() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the create costumer
        Costumer c = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, costumer.getEmail());


            rs = pstmt.executeQuery();

            if (rs.next()) {
                // id,  email,  password,  name,  surname,  address,  latitude,  longitude,  zipcode,  city,  signup_timestamp
                c = new Costumer(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getInt("zipcode"),
                        rs.getString("city"),
                        rs.getInt("chip"),
                        rs.getFloat("spending"),
                        rs.getInt("session_count")
                );
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            //con.close();
        }

        return c;
    }
}
