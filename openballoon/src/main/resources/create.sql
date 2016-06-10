CREATE TABLE Event (
	id SERIAL PRIMARY KEY,
	event_name character varying(255) NOT NULL,
	event_timestamp timestamp,
	location character varying(255),
	geom geometry
);

CREATE TABLE Balloon (
	id SERIAL PRIMARY KEY,
	IDREF_event integer REFERENCES Event(id) ON DELETE CASCADE,
	nr integer NOT NULL,
	properties character varying(255)
);

CREATE TABLE Find (
	id SERIAL PRIMARY KEY,
	IDREF_balloon integer REFERENCES Balloon(id) ON DELETE CASCADE,
	find_timestamp timestamp,
	location character varying(255),
	geom geometry,
	remark character varying(255)
);


