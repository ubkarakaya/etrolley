package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.Costumer;
import it.unipd.dei.webapp.resource.Supermarket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Supermarket from database
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */
public class GetSupermarketDatabase {

    /**
     * The SQL statement to be executed
     */

    private static final String STATEMENT = "select supermarket.*, count(product.id) as product_number from supermarket left join product on supermarket.vatcode = product.supermarket_vatcode\n" +
            "where supermarket.vatcode = ? \n" +
            "group by supermarket.vatcode;";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The attributes
     */
    private final String vatcode;

    public GetSupermarketDatabase(Connection con, String vatcode) {
        this.con = con;
        this.vatcode = vatcode;
    }

    public Supermarket getSupermarket() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Supermarket object
        Supermarket s = null;

        try{
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, vatcode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                s = new Supermarket(
                        rs.getString("vatcode"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getInt("zipcode"),
                        rs.getString("city"),
                        rs.getString("logo"),
                        rs.getInt("rating"),
                        rs.getInt("product_number")
                );
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            //con.close();
        }

        return s;
    }
}
