package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists products in the database.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class ListProductDatabase {

        /**
         * The SQL statement to be executed
         */
        private static final String STATEMENT = "SELECT * FROM product WHERE supermarket_vatcode = ?";

        /**
         * The connection to the database
         */
        private final Connection con;

        /**
         * The supermarket_vatcode
         */
        private final String supermarket_vatcode;
        private final int category_id;
        private final String search;
        private final String orderBy;


    /**
         * Creates a new object for list products
         *  @param con
         *            the connection to the database.
         * @param supermarket_vatcode
         *            the supermarket_vatcode
         */
	public ListProductDatabase(final Connection con, final int category_id, final String search, final String supermarket_vatcode, final String orderBy) {
            this.con = con;
            this.category_id = category_id;
            this.search = search;
            this.supermarket_vatcode = supermarket_vatcode;
            this.orderBy = orderBy;
        }

        /**
         * Lists products.
         *
         * @return a list of {@code Products} object.
         *
         * @throws SQLException
         *             if any error occurs while searching for products.
         */
        public List<Product> listProduct() throws SQLException {

            PreparedStatement pstmt = null;
            ResultSet rs = null;

            final List<Product> products = new ArrayList<Product>();

            String dynamicQuery = STATEMENT;

            // Parameters indexes
            int nParams = 1;
            int categoryParamIndex = 0;
            int searchParamIndex = 0;

            if(category_id > 0){
                nParams++;
                dynamicQuery = dynamicQuery.concat(" AND category_id = ?");
                categoryParamIndex = nParams;
            }

            if(search != null){
                nParams++;
                dynamicQuery = dynamicQuery.concat(" AND LOWER(name) LIKE LOWER(?)");
                searchParamIndex = nParams;
            }

            dynamicQuery = dynamicQuery.concat(" ORDER BY " + orderBy);

            try {
                pstmt = con.prepareStatement(dynamicQuery);

                pstmt.setString(1, supermarket_vatcode);

                if(category_id > 0){
                    pstmt.setInt(categoryParamIndex, category_id);
                }

                if(search != null) {
                    pstmt.setString(searchParamIndex, "%" + search + "%");
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("photo"),
                            rs.getString("description"),
                            rs.getFloat("quantity"),
                            rs.getFloat("unit_price"),
                            rs.getString("measurement_unit"),
                            rs.getString("supermarket_vatcode"),
                            rs.getInt("category_id")));
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

            return products;
        }
}
