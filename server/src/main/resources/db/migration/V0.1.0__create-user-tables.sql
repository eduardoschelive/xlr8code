CREATE TABLE users(
    id uuid PRIMARY KEY NOT NULL,
    email VARCHAR(255)  UNIQUE NOT NULL,
    password_hash VARCHAR(72) NOT NULL,
    password_salt VARCHAR(72) NOT NULL,
    active BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP,
    activated_at TIMESTAMP
);

CREATE TABLE user_metadata(
    user_id uuid NOT NULL,
    language_preference varchar(5) NOT NULL,
    theme_preference varchar(16) NOT NULL,
    profile_picture_url text
);

ALTER TABLE user_metadata ADD FOREIGN KEY (user_id) REFERENCES users (id);