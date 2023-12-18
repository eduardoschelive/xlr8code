CREATE TABLE roles(
    id serial PRIMARY KEY,
    name varchar(32) NOT NULL
);

CREATE TABLE user_role(
    user_id uuid NOT NULL,
    role_id serial NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);