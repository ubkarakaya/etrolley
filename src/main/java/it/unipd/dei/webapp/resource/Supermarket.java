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


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Represents the data about a Supermarket.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class Supermarket extends Resource {

	/**
	 * The vatcode of the supermarket
	 */
	private final String vatcode;
	private final String name;
	private final String address;
	private final float latitude;
	private final float longitude;
	private final int zipcode;
	private final String city;
	private final String logo;
	private final int rating;
	private final float distance;
	private final int product_number;

	public Supermarket(String vatcode, String name, String address, float latitude, float longitude, int zipcode, String city, String logo, int rating, float distance) {
		this.vatcode = vatcode;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zipcode = zipcode;
		this.city = city;
		this.logo = logo;
		this.rating = rating;
		this.distance = distance;
		this.product_number = -1;
	}

	public Supermarket(String vatcode, String name, String address, float latitude, float longitude, int zipcode, String city, String logo, int rating, int product_number) {
		this.vatcode = vatcode;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.zipcode = zipcode;
		this.city = city;
		this.logo = logo;
		this.rating = rating;
		this.distance = -1;
		this.product_number = product_number;
	}

	public String getVatcode() {
		return vatcode;
	}

	public String getName() {
		return name;
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

	public int getZipcode() {
		return zipcode;
	}

	public String getCity() {
		return city;
	}

	public String getLogo() {
		return logo;
	}

	public int getRating() {
		return rating;
	}

	public float getDistance(){return distance; }

	@Override
	public final void toJSON(final OutputStream out) throws IOException {

		final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

		jg.writeStartObject();

		jg.writeFieldName("supermarket");

		jg.writeStartObject();

		jg.writeStringField("vatcode", vatcode);
		jg.writeStringField("name", name);
		jg.writeStringField("address", address);
		jg.writeNumberField("latitude", latitude);
		jg.writeNumberField("longitude", longitude);
		jg.writeStringField("city", city);
		jg.writeNumberField("zipcode", zipcode);
		jg.writeStringField("logo", logo);
		jg.writeNumberField("rating", rating);

		if(distance > -1) {
			jg.writeNumberField("distance", distance);
		}

		if(product_number > -1){
			jg.writeNumberField("product_number", product_number);
		}

		jg.writeEndObject();

		jg.writeEndObject();

		jg.flush();
	}

	/**
	 * Creates a {@code Supermarket} from its JSON representation.
	 *
	 * @param in the input stream containing the JSON document.
	 *
	 * @return the {@code Supermarket} created from the JSON representation.
	 *
	 * @throws IOException if something goes wrong while parsing.
	 */
	public static Supermarket fromJSON(final InputStream in) throws IOException {

		// the fields read from JSON
		String vatcode = "";
		String name = "";
		String surname = "";
		String address = "";
		float latitude = -1;
		float longitude = -1;
		int zipcode = -1;
		String city = "";
		String logo = "";
		int rating = 0;
		float distance = 0;


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
					case "vatcode":
						jp.nextToken();
						vatcode = jp.getText();
						break;
					case "name":
						jp.nextToken();
						name = jp.getText();
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
					case "logo":
						jp.nextToken();
						logo = jp.getText();
						break;
					case "rating":
						jp.nextToken();
						rating = jp.getIntValue();
						break;
					case "distance":
						jp.nextToken();
						distance = jp.getFloatValue();
						break;
				}
			}
		}

		return new Supermarket( vatcode, name, address,  latitude,  longitude,  zipcode,  city,  logo, rating, distance);
	}
}
