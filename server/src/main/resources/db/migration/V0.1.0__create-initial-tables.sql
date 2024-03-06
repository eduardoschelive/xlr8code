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
    profile_picture_url TEXT,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE user_preferences
(
    user_id  UUID NOT NULL,
    language TEXT NOT NULL,
    theme    TEXT NOT NULL,
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
    session_token   TEXT               NOT NULL UNIQUE,
    expires_at      TIMESTAMP          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE user_codes
(
    code_id    SERIAL PRIMARY KEY,
    user_id    UUID      NOT NULL,
    code       TEXT      NOT NULL,
    code_type  TEXT      NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE series
(
    series_id   UUID PRIMARY KEY
);

CREATE TABLE i18n_series
(
    id            UUID PRIMARY KEY,
    series_id     UUID REFERENCES series (series_id),
    language TEXT NOT NULL,
    title          TEXT NOT NULL,
    slug        TEXT NOT NULL UNIQUE,
    description   TEXT,
    CONSTRAINT unique_series_language UNIQUE (series_id, language)
);

CREATE TABLE sections
(
    section_id          UUID PRIMARY KEY,
    series_id           UUID REFERENCES series (series_id),
    order_within_series SMALLINT NOT NULL
);

CREATE TABLE i18n_sections
(
    id            UUID PRIMARY KEY,
    section_id    UUID REFERENCES sections (section_id),
    language TEXT NOT NULL,
    label         TEXT NOT NULL,
    slug          TEXT NOT NULL UNIQUE,
    CONSTRAINT unique_section_language UNIQUE (section_id, language)
);

CREATE TABLE article
(
    article_id           UUID PRIMARY KEY,
    section_id           UUID REFERENCES sections (section_id),
    order_within_section SMALLINT NOT NULL,
    previous_post_id     UUID REFERENCES article (article_id),
    next_post_id         UUID REFERENCES article (article_id)
);

CREATE TABLE i18n_articles
(
    id            UUID PRIMARY KEY,
    article_id    UUID REFERENCES article (article_id),
    language TEXT NOT NULL,
    title         TEXT NOT NULL,
    slug         TEXT NOT NULL UNIQUE,
    content       TEXT NOT NULL,
    CONSTRAINT unique_article_language UNIQUE (article_id, language)
);