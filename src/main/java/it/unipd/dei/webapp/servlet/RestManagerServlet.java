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

import it.unipd.dei.webapp.auth.AuthException;
import it.unipd.dei.webapp.auth.AuthManager;
import it.unipd.dei.webapp.auth.TokenCache;
import it.unipd.dei.webapp.database.CostumerLoginDatabase;
import it.unipd.dei.webapp.resource.*;
import it.unipd.dei.webapp.rest.CategoryRestResource;
import it.unipd.dei.webapp.rest.CostumerRestResource;
import it.unipd.dei.webapp.rest.ProductRestResource;
import it.unipd.dei.webapp.rest.SupermarketRestResource;
import org.mindrot.jbcrypt.BCrypt;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Manages the REST API for the different REST resources.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 *
 */

public final class RestManagerServlet extends AbstractDatabaseServlet {

	/**
	 * The JSON MIME media type
	 */
	private static final String JSON_MEDIA_TYPE = "application/json";

	/**
	 * The JSON UTF-8 MIME media type
	 */
	private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

	private TokenCache tokenCache;

	/**
	 * The any MIME media type
	 */
	private static final String ALL_MEDIA_TYPE = "*/*";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Make the tokens expire after 15 minutes
		tokenCache = new TokenCache(15);
	}

	@Override
	protected final void service(final HttpServletRequest req, final HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType(JSON_UTF_8_MEDIA_TYPE);
		final OutputStream out = res.getOutputStream();

		try {
			// if the request method and/or the MIME media type are not allowed, return.
			// Appropriate error message sent by {@code checkMethodMediaType}
			if (!checkMethodMediaType(req, res)) {
				return;
			}


			// if the requested resource was a costumer, delegate its processing and return
			if (processCostumer(req, res)) {
				return;
			}

			// if the requested resource was an Supermarket, delegate its processing and return
			if (processSupermarket(req, res)) {
				return;
			}

			// if the requested resource was a product, delegate its processing and return
			if (processProduct(req, res)) {
				return;
			}

			// if the requested resource was a product, delegate its processing and return
			if (processCategory(req, res)) {
				return;
			}


			// if none of the above process methods succeeds, it means an unknow resource has been requested
			final Message m = new Message("Unknown resource requested.", "E4A6",
					String.format("Requested resource is %s.", req.getRequestURI()));
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			m.toJSON(out);
		}
		catch (AuthException e){
			// A request needed to be logged in but the client was not.
			final Message m = new Message(e.getMessage(), "401", "401 Unauthorized");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			m.toJSON(out);
		}
		finally {
			// ensure to always flush and close the output stream
			out.flush();
			out.close();
		}
	}

	/**
	 * Checks that the request method and MIME media type are allowed.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request method and the MIME type are allowed; {@code false} otherwise.
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	private boolean checkMethodMediaType(final HttpServletRequest req, final HttpServletResponse res)
			throws IOException {

		final String method = req.getMethod();
		final String contentType = req.getHeader("Content-Type");
		final String accept = req.getHeader("Accept");
		final OutputStream out = res.getOutputStream();

		Message m = null;

		if (accept == null) {
			m = new Message("Output media type not specified.", "E4A1", "Accept request header missing.");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(out);
			return false;
		}

		if (!accept.contains(JSON_MEDIA_TYPE) && !accept.equals(ALL_MEDIA_TYPE)) {
			m = new Message("Unsupported output media type. Resources are represented only in application/json.",
					"E4A2", String.format("Requested representation is %s.", accept));
			res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			m.toJSON(out);
			return false;
		}

		switch (method) {
			case "GET":
			case "DELETE":
				// nothing to do
				break;

			case "POST":
			case "PUT":
				if (contentType == null) {
					m = new Message("Input media type not specified.", "E4A3", "Content-Type request header missing.");
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					m.toJSON(out);
					return false;
				}

				if (!contentType.contains(JSON_MEDIA_TYPE)) {
					m = new Message("Unsupported input media type. Resources are represented only in application/json.",
							"E4A4", String.format("Submitted representation is %s.", contentType));
					res.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
					m.toJSON(out);
					return false;
				}

				break;
			default:
				m = new Message("Unsupported operation.",
						"E4A5", String.format("Requested operation %s.", method));
				res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				m.toJSON(out);
				return false;
		}

		return true;
	}


	/**
	 * Checks whether the request if for an {@link Costumer} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Costumer}; {@code false} otherwise.
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	private boolean processCostumer(HttpServletRequest req, HttpServletResponse res) throws IOException, AuthException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		// the requested resource was not a costumer
		if (path.lastIndexOf("rest/costumer") <= 0) {
			return false;
		}

		try {
			// strip everyhing until after the /costumer
			path = path.substring(path.lastIndexOf("costumer") + 8);

			// the request URI is: /costumer
			// if method POST, create costumer
			if (path.length() == 0 || path.equals("/")) {

				switch (method) {
					case "POST":
						new CostumerRestResource(req, res, getDataSource().getConnection()).createCostumer(tokenCache);
						break;
					default:
						m = new Message("Unsupported operation for URI /costumer.",
								"E4A5", String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}

			}
			// Login actions
			else if(path.equals("/login")){
				switch (method){
					case "POST":
						// do the login
						this.doLogin(req,res);
						break;

					case "DELETE":
						// Do the logout
						this.doLogout(getBearerToken(req), res);
						break;
				}
			}
		}
		catch(AuthException e){
			throw e;
		}
		catch(Throwable t) {
			m = new Message("Unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());

		}

		return true;

	}

	/**
	 * Checks whether the request if for an {@link Supermarket} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Supermarket}; {@code false} otherwise.
	 * @throws IOException if any error occurs in the client/server communication.
	 * @throws AuthException if the user is not logged in
	 */
	private boolean processSupermarket(HttpServletRequest req, HttpServletResponse res) throws IOException, AuthException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		String basePath = "rest/supermarket";
		int pathIndex;
		// the requested resource was not a supermarket
		if ( (pathIndex = path.lastIndexOf(basePath)) <= 0) {
			return false;
		}

		// Remove app name from path variable
		String restPath = path.substring(pathIndex);

		// Require auth
		Costumer authUser = getAuthUserFromRequest(req);

		// Split url into parts
		String[] parts  = restPath.split("/");

		try {
			if(parts.length == 3){
				// Take single supermarket
				switch (method) {
					case "GET":
						// Get vatcode from url
						String supermarketVat = parts[2];
						new SupermarketRestResource(req, res, getDataSource().getConnection()).getSupermarket(supermarketVat);
						break;
					default:
						m = new Message("Unsupported operation for URI /supermarket/{vatcode}", "E4A5",
								String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}
			}
			else if(parts.length == 2) {
				// Take the supermarket list
				switch (method) {
					case "GET":
						new SupermarketRestResource(req, res, getDataSource().getConnection()).listSupermarket();
						break;
					default:
						m = new Message("Unsupported operation for URI /supermarket/", "E4A5",
								String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}
			}
			else{
				return false;
			}

		} catch (Throwable t) {

			m = new Message("Unexpected error.", "E5A1", t.toString());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
		return true;
	}

	/**
	 * Checks whether the request if for an {@link Product} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Product}; {@code false} otherwise.
	 * @throws IOException if any error occurs in the client/server communication.
	 * @throws AuthException if the user is not logged in
	 */
	private boolean processProduct(HttpServletRequest req, HttpServletResponse res) throws IOException, AuthException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		// the requested resource was not a supermarket
		if (path.lastIndexOf("rest/product") <= 0) {
			return false;
		}

		// Require auth
		Costumer authUser = getAuthUserFromRequest(req);

		try {
			// strip everyhing until after the /product
			// /rest/product/supermarket/1
			if((path.lastIndexOf("rest/product/supermarket/") >0 ) && method.equals("GET") ){
				String supermarketVat = path.substring(path.lastIndexOf('/') + 1);
				new ProductRestResource(req,res,getDataSource().getConnection()).listProduct(supermarketVat);
			}
			else{
				return false;
			}

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Checks whether the request if for an {@link Category} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Category}; {@code false} otherwise.
	 * @throws IOException if any error occurs in the client/server communication.
	 * @throws AuthException if the user is not logged in
	 */
	private boolean processCategory(HttpServletRequest req, HttpServletResponse res) throws IOException, AuthException {
		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		// the requested resource was not a supermarket
		if (path.lastIndexOf("rest/category") <= 0) {
			return false;
		}

		// Require auth
		Costumer authUser = getAuthUserFromRequest(req);

		try {
			if(method.equals("GET") ){
				new CategoryRestResource(req,res,getDataSource().getConnection()).listCategories();
			}
			else{
				return false;
			}

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Performs the login with credentials sent by the user in the request body
	 * @param httpRequest
	 * @param httpResponse
	 * @throws AuthException if login fields are invalid
	 * @throws IOException if there is an I/O error
	 */
	private void doLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws AuthException, IOException {
		Costumer c  = null; // The costumer obtained by "email" sent by the client
		Message m = null;

		try{
			final CostumerLogin cForLogin =  CostumerLogin.fromJSON(httpRequest.getInputStream());
			// creates a new object for accessing the database and tries to do the login
			c = new CostumerLoginDatabase(getDataSource().getConnection(), cForLogin).getCostumerForLogin();
			if(c == null) {
				throw new AuthException("Costumer email is not valid");
			}

			if( ! BCrypt.checkpw(cForLogin.getPassword(), c.getPassword()) ){
				throw new AuthException("Costumer password is not valid");
			}
			AuthManager authMng = new AuthManager(getDataSource().getConnection());
			Token userToken = authMng.generateTokenForCostumer(c);
			tokenCache.addCostumer(userToken.getToken(), c);
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			// Send also the token along with the user
			c.toJSONWithToken(httpResponse.getOutputStream(), userToken);
		}
		catch (AuthException e){
			throw e;
		}
		catch (Throwable t){
			m = new Message("Login unexpected error.", "E5A1", t.getMessage());
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(httpResponse.getOutputStream());
		}
	}

	/**
	 * Performs the user logout
	 * @param token
	 * @param httpResponse
	 * @throws IOException
	 */
	private void doLogout(String token, HttpServletResponse httpResponse) throws IOException {
		if(token != null) {
			tokenCache.removeCostumer(token);
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			httpResponse.getOutputStream().println("\"ok\"");
		}
		else{
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	/**
	 * Returns the Costumer object from the token in the request
	 * @param httpRequest
	 * @return
	 * @throws AuthException if the token is not valid or not provided
	 */
	private Costumer getAuthUserFromRequest(HttpServletRequest httpRequest) throws AuthException {
		String token = getBearerToken(httpRequest);

		if(null == token){
			throw new AuthException("Bearer token not received or Authorization header is malformed");
		}

		Costumer c = tokenCache.getCostumer(token);

		if(null == c){
			throw new AuthException("Bearer Token expired or invalid: "+ token);
		}

		return c;
	}

	/**
	 * Returns the bearer token from Authorization header
	 * If the token does not exist it returns null
	 * @param httpRequest
	 * @return the token string
	 */
	private String getBearerToken(HttpServletRequest httpRequest){
		String authHeader = httpRequest.getHeader("Authorization");

		if(null == authHeader){
			return null;
		}

		if(! authHeader.startsWith("Bearer ")){
			return null;
		}

		return authHeader.split(" ")[1].trim();
	}


	@Override
	public void destroy() {
		super.destroy();

		// Destroys the token cache
		tokenCache = null;
	}
}









