CREATE TABLE users
(
    user_id      UUID PRIMARY KEY         NOT NULL,
    username     TEXT UNIQUE              NOT NULL,
    email        TEXT UNIQUE              NOT NULL,
    password     VARCHAR(60)              NOT NULL,
    active       BOOLEAN                  NOT NULL DEFAULT false,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    activated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE user_metadata
(
    user_id             UUID NOT NULL,
    language_preference TEXT NOT NULL,
    theme_preference    TEXT NOT NULL,
    profile_picture_url TEXT,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE roles
(
    role_id serial PRIMARY KEY,
    name    TEXT NOT NULL
);

CREATE TABLE user_role
(
    user_id UUID   NOT NULL,
    role_id SERIAL NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

CREATE TABLE user_sessions
(
    user_session_id SERIAL PRIMARY KEY NOT NULL,
    user_id         UUID               NOT NULL,
    session_token   UUID               NOT NULL UNIQUE,
    expires_at      TIMESTAMP          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE user_activation_codes
(
    activation_code_id SERIAL PRIMARY KEY NOT NULL,
    user_id            UUID               NOT NULL,
    code               TEXT               NOT NULL UNIQUE,
    expires_at         TIMESTAMP          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE user_password_reset_codes
(
    password_reset_code_id SERIAL PRIMARY KEY NOT NULL,
    user_id                UUID               NOT NULL,
    code                   TEXT               NOT NULL UNIQUE,
    expires_at             TIMESTAMP          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);