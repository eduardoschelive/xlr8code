CREATE TABLE users(
    id UUID PRIMARY KEY NOT NULL,
    username TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    active BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    activated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE user_metadata(
    user_id UUID NOT NULL,
    language_preference VARCHAR(5) NOT NULL default 'en-US',
    theme_preference TEXT NOT NULL default 'system',
    profile_picture_url TEXT,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

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