CREATE TABLE IF NOT EXISTS members (
    id uuid PRIMARY KEY,
    name varchar(255),
    email varchar(255),
    version bigint,
    created_by varchar(255),
    created_date timestamp,
    last_modified_by varchar(255),
    last_modified_date timestamp
);
