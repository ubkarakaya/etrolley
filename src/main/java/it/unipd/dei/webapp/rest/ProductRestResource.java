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
import it.unipd.dei.webapp.database.ListProductDatabase;
import it.unipd.dei.webapp.resource.Costumer;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.Product;
import it.unipd.dei.webapp.resource.ResourceList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Manages the REST API for the {@link Product} resource.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 *
 */

public final class ProductRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Product} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public ProductRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}


	/**
	 * Lists all the products in json format to output stream
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */

	public void listProduct(String supermarketVat) throws IOException {

		List<Product> pl  = null;
		Message m = null;

		try{
			String rawCategory = req.getParameter("category");
			int categoryId = -1;
			if(null != rawCategory){
				categoryId = Integer.parseInt(rawCategory);
			}
			String search = req.getParameter("search");
			String rawOrderBy = req.getParameter("orderBy");
			String orderBy = "";

			if(rawOrderBy != null && rawOrderBy.equals("price-desc")){
				orderBy += "unit_price DESC";
			}
			else{
				orderBy += "unit_price ASC";
			}

			// creates a new object for accessing the database and lists all the products
			pl = new ListProductDatabase(con,categoryId,search,supermarketVat,orderBy).listProduct();

			if(pl != null) {
				res.setStatus(HttpServletResponse.SC_OK);
				new ResourceList<Product>(pl).toJSON(res.getOutputStream());
			} else {
				// it should not happen
				m = new Message("Cannot list products: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
		} catch (Throwable t) {
			m = new Message("Cannot search product: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}
}
