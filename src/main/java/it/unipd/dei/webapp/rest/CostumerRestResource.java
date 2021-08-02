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

package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.auth.AuthManager;
import it.unipd.dei.webapp.auth.TokenCache;
import it.unipd.dei.webapp.database.*;
import it.unipd.dei.webapp.resource.*;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages the REST API for the {@link Costumer} resource.
 * 
 * @author eTrolley Group
 * @version 1.00
 * @since 1.00
 */

public final class CostumerRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Customer} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public CostumerRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}


	/**
	 * Creates a new costumer into the database and saves it inside tokencache
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */
	public void createCostumer(TokenCache tokenCache) throws IOException {

		Costumer c  = null;
		Message m = null;

		try{

			final Costumer costumer = Costumer.fromJSON(req.getInputStream());

			// creates a new object for accessing the database and stores the costumer
			c = new CreateCostumerDatabase(con, costumer).createCostumer();

			if(c != null) {
				// Create token, save to cache and return the user
				AuthManager authManager = new AuthManager(con);
				Token token = authManager.generateTokenForCostumer(c);
				tokenCache.addCostumer(token.getToken(), c);

				res.setStatus(HttpServletResponse.SC_CREATED);
				c.toJSONWithToken(res.getOutputStream(), token);
			} else {
				// it should not happen
				m = new Message("Cannot create the costumer: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
		} catch (Throwable t) {
			if (t instanceof SQLException
			 && ((SQLException) t).getSQLState() != null
			 && ((SQLException) t).getSQLState().equals("23505")) {
				m = new Message("Cannot create the costumer: it already exists.", "E5A2", t.getMessage());
				res.setStatus(HttpServletResponse.SC_CONFLICT);
			} else {
				m = new Message("Cannot create the costumer: unexpected error.", "E5A1", t.getMessage());
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			m.toJSON(res.getOutputStream());
		}
	}

}
