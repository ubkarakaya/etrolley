/*
 * Copyright 2018 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.database.ConnectionHelper;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 * Gets the {@code DataSource} for managing the connection pool to the database.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 *
 */
public abstract class AbstractDatabaseServlet extends HttpServlet {

	/**
	 * The connection pool to the database.
	 */
	private DataSource ds;

	/**
     * Gets the {@code DataSource} for managing the connection pool to the database.
     *
	 * @param config
     *          a {@code ServletConfig} object containing the servlet's
     *          configuration and initialization parameters.
	 *
	 * @throws ServletException
     *          if an exception has occurred that interferes with the servlet's normal operation
	 */
    public void init(ServletConfig config) throws ServletException {
        ds = ConnectionHelper.getDataSource();
    }

    /**
     * Releases the {@code DataSource} for managing the connection pool to the database.
     */
    public void destroy() {
        ds = null;
    }

    /**
     * Returns the {@code DataSource} for managing the connection pool to the database.
     *
     * @return the {@code DataSource} for managing the connection pool to the database
     */
    protected final DataSource getDataSource() {
        return ds;
    }

}
