package it.unipd.dei.webapp.database;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 * Connection Helper.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class ConnectionHelper {
    private static DataSource ds = null;

    public static DataSource getDataSource() throws ServletException {
        if(null != ds ){
            return ds;
        }

        // the JNDI lookup context
        InitialContext cxt;

        try {
            cxt = new InitialContext();
            ///comp/env/
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/costumer-etrolley");
            return ds;
        } catch (NamingException e) {
            ds = null;

            throw new ServletException(
                    String.format("Impossible to access the connection pool to the database: %s \n %s",
                            e.getMessage(),  e.getRootCause().toString()));
        }
    }
}
