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

CREATE TABLE category
(
    category_id UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE i18n_category
(
    id          UUID PRIMARY KEY,
    category_id UUID REFERENCES category (category_id),
    language    TEXT NOT NULL,
    title       TEXT NOT NULL,
    slug        TEXT NOT NULL UNIQUE,
    description TEXT,
    CONSTRAINT unique_category_language UNIQUE (category_id, language)
);

CREATE TABLE articles
(
    article_id          UUID PRIMARY KEY,
    previous_article_id UUID REFERENCES articles (article_id),
    next_article_id     UUID REFERENCES articles (article_id),
    category_id         UUID,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    position            INT,
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

CREATE TABLE i18n_articles
(
    id         UUID PRIMARY KEY,
    article_id UUID REFERENCES articles (article_id),
    language   TEXT NOT NULL,
    title      TEXT NOT NULL,
    slug       TEXT NOT NULL UNIQUE,
    content    TEXT NOT NULL,
    CONSTRAINT unique_article_language UNIQUE (article_id, language)
);