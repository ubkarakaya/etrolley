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

import it.unipd.dei.webapp.database.CreateCostumerDatabase;
import it.unipd.dei.webapp.database.GetSupermarketDatabase;
import it.unipd.dei.webapp.database.ListSupermarketDatabase;
import it.unipd.dei.webapp.resource.Costumer;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.ResourceList;
import it.unipd.dei.webapp.resource.Supermarket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Manages the REST API for the {@link Supermarket} resource.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 *
 */

public final class SupermarketRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Supermarket} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public SupermarketRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}

	/**
	 * Returns the supermarket associated to the vatcode in json format to
	 * the output stream
	 * @param vatCode
	 * @throws IOException
	 */
	public void getSupermarket(String vatCode) throws IOException {
		Supermarket s  = null;
		Message m = null;

		// creates a new object for accessing the database and stores the costumer
		try {
			s = new GetSupermarketDatabase(con, vatCode).getSupermarket();

			if(s != null) {
				res.setStatus(HttpServletResponse.SC_OK);
				s.toJSON(res.getOutputStream());
			} else {
				// it should not happen
				m = new Message("Cannot get supermarket: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
		}
		catch (Throwable t) {
			m = new Message("Cannot get supermarket: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}


	/**
	 * Lists all the supermarkets in json format to the output stram
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */

	public void listSupermarket() throws IOException {

		List<Supermarket> sl  = null;
		Message m = null;

		try{
			float latitude = Float.parseFloat( req.getParameter("latitude") );
			float longitude = Float.parseFloat( req.getParameter("longitude") );
			String search = req.getParameter("search");

			// creates a new object for accessing the database and lists all the supermarkets
			sl = new ListSupermarketDatabase(con, latitude, longitude, search).listSupermarket();

			if(sl != null) {
				res.setStatus(HttpServletResponse.SC_OK);
				new ResourceList<Supermarket>(sl).toJSON(res.getOutputStream());
			} else {
				// it should not happen
				m = new Message("Cannot list supermarkets: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}

		}
		catch (NumberFormatException e){
			m = new Message("Cannot search supermarket: latitude or longitude must be float values.", "E5A1", e.toString() + " " + e.getCause().toString());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
		catch (Throwable t) {
			m = new Message("Cannot search supermarket: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
			/*
			for(StackTraceElement el : t.getStackTrace()){
				res.getOutputStream().println(el.getFileName());
				res.getOutputStream().println(el.getLineNumber());
			}*/
		}
	}

}
