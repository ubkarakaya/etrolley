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
 * Represents the data about a Product.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class Product extends Resource {

	/**
	 * The id of the product
	 */
	private final int id;
	private final String name;
	private final String photo;
	private final String description;
	private final float quantity;
	private final float unit_price;
	private final String measurement_unit;
	private final String supermarket_vatcode;
	private final int category_id;

	public Product(int id, String name, String photo, String description, float quantity, float unit_price, String measurement_unit, String supermarket_vatcode, int category_id) {
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.description = description;
		this.quantity = quantity;
		this.unit_price = unit_price;
		this.measurement_unit = measurement_unit;
		this.supermarket_vatcode = supermarket_vatcode;
		this.category_id = category_id;
	}

	public int getId(){
		return id;
	}

	public String getPhoto() {
		return photo;
	}

	public String getDescription() {
		return description;
	}

	public float getQuantity() {
		return quantity;
	}

	public float getUnit_price() {
		return unit_price;
	}

	public String getMeasurement_unit() {
		return measurement_unit;
	}

	public String getSupermarket_vatcode() {
		return supermarket_vatcode;
	}

	public int getCategory_id() {
		return category_id;
	}

	@Override
	public final void toJSON(final OutputStream out) throws IOException {

		final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

		jg.writeStartObject();

		jg.writeFieldName("product");

		jg.writeStartObject();

		jg.writeNumberField("id", id);
		jg.writeStringField("name", name);
		jg.writeStringField("photo", photo);
		jg.writeStringField("description", description);
		jg.writeNumberField("quantity", quantity);
		jg.writeNumberField("unit_price", unit_price);
		jg.writeStringField("measurement_unit", measurement_unit);
		jg.writeStringField("supermarket_vatcode", supermarket_vatcode);
		jg.writeNumberField("category_id", category_id);

		jg.writeEndObject();

		jg.writeEndObject();

		jg.flush();
	}

	/**
	 * Creates a {@code Product} from its JSON representation.
	 *
	 * @param in the input stream containing the JSON document.
	 *
	 * @return the {@code Product} created from the JSON representation.
	 *
	 * @throws IOException if something goes wrong while parsing.
	 */
	public static Product fromJSON(final InputStream in) throws IOException {

		// the fields read from JSON
		int id = -1;
		String name = "";
		String photo = "";
		String description = "";
		float quantity = -1;
		float unit_price = -1;
		String measurement_unit = "";
		String supermarket_vatcode = "";
		int category_id = -1;


		final JsonParser jp = JSON_FACTORY.createParser(in);

		// while we are not on the start of an element or the element is not
		// a token element, advance to the next element (if any)
		while (jp.getCurrentToken() != JsonToken.FIELD_NAME || "product".equals(jp.getCurrentName()) == false) {

			// there are no more events
			if (jp.nextToken() == null) {
				throw new IOException("Unable to parse JSON: no product object found.");
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
					case "photo":
						jp.nextToken();
						photo = jp.getText();
						break;
					case "description":
						jp.nextToken();
						description = jp.getText();
						break;
					case "quantity":
						jp.nextToken();
						quantity = jp.getFloatValue();
						break;
					case "unit_price":
						jp.nextToken();
						unit_price = jp.getFloatValue();
						break;
					case "measurement_unit":
						jp.nextToken();
						measurement_unit = jp.getText();
						break;
					case "supermarket_vatcode":
						jp.nextToken();
						supermarket_vatcode = jp.getText();
						break;
					case "category_id":
						jp.nextToken();
						category_id = jp.getIntValue();
						break;
				}
			}
		}

		return new Product( id,  name,  photo,  description,  quantity,  unit_price,  measurement_unit,  supermarket_vatcode,  category_id);
	}
}
