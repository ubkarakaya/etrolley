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

package it.unipd.dei.webapp.resource;


import com.fasterxml.jackson.core.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Represents the data about a Costumer.
 * 
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class Costumer extends Resource {

	/**
	 * The id of the costumer
	 */
	private final int id;
	private final String email;
	private final String password;
	private final String name;
	private final String surname;
	private final String address;
	private final float latitude;
	private final float longitude;
	private final int zipcode;
	private final String city;
	private final int chip;
	private final float spending;
	private final int session_count;


	public Costumer(int id, String email, String password, String name, String surname, String address, float latitude, float longitude, int zipcode, String city, int chip, float spending, int session_count) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zipcode = zipcode;
		this.city = city;
		this.chip = chip;
		this.spending = spending;
		this.session_count = session_count;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getAddress() {
		return address;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public int getChip() {
		return chip;
	}

	public float getSpending() {
		return spending;
	}

	public int getSession_count() {
		return session_count;
	}

	public int getZipcode() {
		return zipcode;
	}

	public String getCity() {
		return city;
	}

	@Override
	public final void toJSON(final OutputStream out) throws IOException {

		final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

		jg.writeStartObject();
		this.constructCostumerJson(jg);
		jg.writeEndObject();

		jg.flush();
	}

	/**
	 * Prints JSON representation of the costumer plus the token passed as parameter
	 * To be used as costumer login response
	 * @param out
	 * @param token
	 * @throws IOException
	 */
	public final void toJSONWithToken(final OutputStream out, final Token token) throws IOException{
		final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

		jg.writeStartObject();
		this.constructCostumerJson(jg);
		this.constructTokenJson(jg,token);
		jg.writeEndObject();

		jg.flush();
	}

	private void constructCostumerJson(final JsonGenerator jg) throws IOException {
		jg.writeFieldName("costumer");

		jg.writeStartObject();

		jg.writeNumberField("id", id);

		jg.writeStringField("name", name);
		jg.writeStringField("surname", surname);
		jg.writeStringField("email", email);
		jg.writeStringField("address", address);
		jg.writeNumberField("latitude", latitude);
		jg.writeNumberField("longitude", longitude);
		jg.writeStringField("city", city);
		jg.writeNumberField("zipcode", zipcode);
		jg.writeNumberField("chip", chip);
		jg.writeNumberField("spending", spending);
		jg.writeNumberField("session_count", session_count);

		jg.writeEndObject();
	}

	private void constructTokenJson(final JsonGenerator jg, final Token token) throws IOException {
		jg.writeFieldName("token");
		jg.writeStartObject();
		jg.writeStringField("bearer", token.getToken());
		jg.writeStringField("expires", token.getExpires().toString());
		jg.writeEndObject();

	}
	/**
	 * Creates a {@code Customer} from its JSON representation.
	 *
	 * @param in the input stream containing the JSON document.
	 *
	 * @return the {@code Customer} created from the JSON representation.
	 *
	 * @throws IOException if something goes wrong while parsing.
	 */
	public static Costumer fromJSON(final InputStream in) throws IOException {

		// the fields read from JSON
		int id = -1;
		String email = "";
		String password = "";
		String name = "";
		String surname = "";
		String address = "";
		float latitude = -1;
		float longitude = -1;
		int zipcode = -1;
		String city = "";


		final JsonParser jp = JSON_FACTORY.createParser(in);

		// while we are not on the start of an element or the element is not
		// a token element, advance to the next element (if any)
		while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "costumer".equals(jp.getCurrentName()) == false) {

			// there are no more events
			if (jp.nextToken() == null) {
				throw new IOException("Unable to parse JSON: no costumer object found.");
			}
		}

		while (jp.nextToken() != JsonToken.END_OBJECT) {

			if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

				switch (jp.getCurrentName()) {
					case "id":
						jp.nextToken();
						id = jp.getIntValue();
						break;
					case "name":
						jp.nextToken();
						name = jp.getText();
						break;
					case "surname":
						jp.nextToken();
						surname = jp.getText();
						break;
					case "email":
						jp.nextToken();
						email = jp.getText();
						break;
					case "password":
						jp.nextToken();
						// Hash the password
						password = BCrypt.hashpw(jp.getText(), BCrypt.gensalt());
						break;
					case "address":
						jp.nextToken();
						address = jp.getText();
						break;
					case "latitude":
						jp.nextToken();
						latitude = jp.getFloatValue();
						break;
					case "longitude":
						jp.nextToken();
						longitude = jp.getFloatValue();
						break;
					case "city":
						jp.nextToken();
						city = jp.getText();
						break;
					case "zipcode":
						jp.nextToken();
						zipcode = jp.getIntValue();
						break;
				}
			}
		}

		// chip, spending and session_count set to default
		return new Costumer( id,  email,  password,  name,  surname,  address,  latitude,  longitude,  zipcode,  city, 100, 0, 0);
	}
}
