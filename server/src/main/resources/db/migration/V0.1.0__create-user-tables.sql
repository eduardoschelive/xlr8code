CREATE TABLE users(
    id UUID PRIMARY KEY NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    active BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP,
    activated_at TIMESTAMP
);

CREATE TABLE user_metadata(
    user_id UUID NOT NULL,
    language_preference VARCHAR(5) NOT NULL default 'en-US',
    theme_preference TEXT NOT NULL default 'system',
    profile_picture_url TEXT,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);