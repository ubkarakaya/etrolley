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

package it.unipd.dei.webapp.database;

import it.unipd.dei.webapp.resource.Costumer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a costumer in the database.
 * 
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public final class CreateCostumerDatabase {

	/**
	 * The SQL statement to be executed
	 */
	private static final String STATEMENT = "INSERT INTO costumer (email,  password,  name,  surname,  address,  latitude,  longitude,  zipcode,  city) VALUES ( ?, ?, ?, ?, ? ,?,?,?,?) RETURNING *";

	/**
	 * The connection to the database
	 */
	private final Connection con;

	/**
	 * The costumer to be inserted in the database
	 */
	private final Costumer costumer;

	/**
	 * Creates a new object for inserting a costumer.
	 * 
	 * @param con
	 *            the connection to the database.
	 * @param costumer
	 *            the costumer to be created in the database.
	 */
	public CreateCostumerDatabase(final Connection con, final Costumer costumer) {
		this.con = con;
		this.costumer = costumer;
	}

	/**
	 * Creates an costumer in the database.
	 * 
	 * @return the {@code Costumer} object matching the id.
	 * 
	 * @throws SQLException
	 *             if any error occurs while reading the customer.
	 */
	public Costumer createCostumer() throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// the create costumer
		Costumer c = null;

		try {
			/* (email,  password,  name,  surname,  address,  latitude,  longitude,  zipcode,  city,  signup_timestamp)*/
			pstmt = con.prepareStatement(STATEMENT);
			pstmt.setString(1, costumer.getEmail());
			pstmt.setString(2, costumer.getPassword());
			pstmt.setString(3, costumer.getName());
			pstmt.setString(4, costumer.getSurname());
			pstmt.setString(5, costumer.getAddress());
			pstmt.setFloat(6, (float) 45.406517); // Forced latitude
			pstmt.setFloat(7, (float) 11.881446); // Forced longitude
			pstmt.setInt(8, costumer.getZipcode());
			pstmt.setString(9, costumer.getCity());


			rs = pstmt.executeQuery();

			if (rs.next()) {
				// id,  email,  password,  name,  surname,  address,  latitude,  longitude,  zipcode,  city,  signup_timestamp
				c = new Costumer(
						rs.getInt("id"),
						rs.getString("email"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("surname"),
						rs.getString("address"),
						rs.getFloat("latitude"),
						rs.getFloat("longitude"),
						rs.getInt("zipcode"),
						rs.getString("city"),
						rs.getInt("chip"),
						rs.getFloat("spending"),
						rs.getInt("session_count")
				);
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

		return c;
	}
}
