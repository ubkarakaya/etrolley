CREATE TABLE costumer(
	id SERIAL PRIMARY KEY NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,  -- Warning must me unique
	password VARCHAR(500) NOT NULL,
	name VARCHAR (100) NOT NULL,
	surname VARCHAR (100) NOT NULL,
	address VARCHAR (255) NOT NULL,
	latitude FLOAT NULL,
	longitude FLOAT NULL,
	zipcode INTEGER NOT NULL,
	city VARCHAR(100) NOT NULL,
	chip INTEGER NOT NULL DEFAULT 100,
    spending MONEY NOT NULL DEFAULT 0,
    session_count INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE supermarket(
	vatcode CHAR(11) PRIMARY KEY NOT NULL,
	name VARCHAR (100) NOT NULL,
	address VARCHAR (255) NOT NULL,
	latitude FLOAT NULL,
	longitude FLOAT NULL,
	zipcode INTEGER NOT NULL,
	city VARCHAR(100) NOT NULL,
	logo VARCHAR (100) NULL,
	rating INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE category(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR (100) NOT NULL
);

CREATE TABLE product(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR (100) NOT NULL,
	photo VARCHAR (100) NULL,
	description TEXT NULL,
	quantity FLOAT NOT NULL,
	unit_price MONEY NOT NULL,
	measurement_unit VARCHAR(10),
	supermarket_vatcode CHAR(11) REFERENCES supermarket(vatcode),
	category_id INTEGER REFERENCES category(id)
);

CREATE TYPE payment_method AS ENUM ('cash', 'credit_card', 'paypal');
CREATE TABLE orders(
	id SERIAL PRIMARY KEY NOT NULL,
	total_price MONEY NOT NULL,
	costumeR_payment_method payment_method NULL,
	order_TIMESTAMP TIMESTAMP NOT NULL,
	payment_TIMESTAMP TIMESTAMP NULL,
	costumer_id INTEGER REFERENCES costumer(id)
);

CREATE TABLE order_products(
	product_id INTEGER REFERENCES product(id),
	order_id INTEGER REFERENCES orders(id)
);

CREATE TABLE shopping_list(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR (100) NOT NULL,
	costumer_id INTEGER REFERENCES costumer(id)
);

CREATE TABLE shopping_list_item(
	id SERIAL PRIMARY KEY NOT NULL,
	name VARCHAR (100) NOT NULL,
	shopping_list_id INTEGER REFERENCES shopping_list(id)
);

CREATE TABLE tokens(
	token CHAR(32) PRIMARY KEY NOT NULL,
	creation_ts TIMESTAMP NOT NULL,
	expires TIMESTAMP NOT NULL,
	costumer_id INTEGER REFERENCES costumer(id)
);