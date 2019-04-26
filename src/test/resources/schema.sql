DROP TABLE names IF EXISTS;
CREATE TABLE names  (
    id VARCHAR(10),
    name VARCHAR(400),
    known_for_Titles VARCHAR(999),
    profession VARCHAR(999),
    PRIMARY KEY (id)
);
DROP TABLE titles IF EXISTS;
CREATE TABLE titles  (
    id VARCHAR (20),
    title VARCHAR(1500),
    type varchar(200),
    genres VARCHAR(999),
    start_year varchar(10),
    weighted_rating decimal(16,2),
    PRIMARY KEY (id)
);
