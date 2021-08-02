package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.Product;
import it.unipd.dei.webapp.resource.Supermarket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists supermarkets in the database.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class ListSupermarketDatabase {

    /**
     * The SQL statement to be executed
     */

    private static final String STATEMENT = "SELECT t.* from(\n" +
            "SELECT *, SQRT(\n" +
            "    POW(69.1 * (latitude - ?), 2) +\n" +
            "    POW(69.1 * (? - longitude) * COS(latitude / 57.3), 2)\n" +
            ") AS distance\n" +
            "FROM supermarket \n" +
            "GROUP BY vatcode\n" +
            ") t\n" +
            "where distance < 25\n";

    /**
     * The connection to the database
     */
    private final Connection con;

    /**
     * The attributes
     */
    private final float latitude;
    private final float longitude;
    private final String search;

    /**
     * Creates a new object for searching employees by salary.
     *  @param con
     *            the connection to the database.
     * @param latitude, longitude, search
     *                  parameters
     */
    public ListSupermarketDatabase(final Connection con, float latitude, float longitude, String search)
    {
        this.con = con;
        this.latitude = latitude;
        this.longitude = longitude;
        this.search = search;
    }

    /**
     * Lists supermarkets.
     *
     * @return a list of {@code Supermarket} object.
     *
     * @throws SQLException
     *             if any error occurs while searching for supermarkets.
     */
    public List<Supermarket> listSupermarket() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Supermarket> supermarkets = new ArrayList<Supermarket>();

        String dynamicQuery = STATEMENT;

        if(search != null){
            dynamicQuery = dynamicQuery.concat(" and LOWER(name) like LOWER(?)");
        }

        dynamicQuery = dynamicQuery.concat(" ORDER BY distance;");

        try {
            pstmt = con.prepareStatement(dynamicQuery);

            pstmt.setFloat(1, latitude);
            pstmt.setFloat(2, longitude);

            if(search != null){
                pstmt.setString(3, "%" + search + "%");
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                supermarkets.add(new Supermarket(
                        rs.getString("vatcode"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getInt("zipcode"),
                        rs.getString("city"),
                        rs.getString("logo"),
                        rs.getInt("rating"),
                        rs.getFloat("distance")
                        )
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

        return supermarkets;
    }
}
