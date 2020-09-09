CREATE TABLE polls (
    id CHAR(36) NOT NULL,
    question VARCHAR(2048) NOT NULL,
    author CHAR(36),
    publication_date DATE,
    PRIMARY KEY (id)
);


CREATE TABLE authors (
    id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE uservotes 
(
    user_id VARCHAR(255) NOT NULL,
    poll_id CHAR(36) NOT NULL,
    liked boolean,
    voted_for boolean,
    voted_against boolean,
    PRIMARY KEY (user_id, poll_id)
);

