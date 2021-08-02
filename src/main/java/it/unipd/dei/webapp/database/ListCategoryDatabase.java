package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.Category;
import it.unipd.dei.webapp.resource.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists categories in the database.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */
public class ListCategoryDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT = "select * from category order by name asc";

    /**
     * The connection to the database
     */
    private final Connection con;

    public ListCategoryDatabase(Connection con) {
        this.con = con;
    }

    /**
     * Lists products.
     *
     * @return a list of {@code Category} object.
     *
     * @throws SQLException
     *             if any error occurs while listing categories.
     */
    public List<Category> listCategory() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Category> categories = new ArrayList<Category>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
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

        return categories;
    }
}
