CREATE TABLE IF NOT EXISTS users
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

CREATE TABLE IF NOT EXISTS user_metadata
(
    user_id             UUID NOT NULL,
    profile_picture_url TEXT,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_preferences
(
    user_id  UUID NOT NULL,
    language TEXT NOT NULL,
    theme    TEXT NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS roles
(
    role_id SERIAL PRIMARY KEY,
    name    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id UUID   NOT NULL,
    role_id SERIAL NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

CREATE TABLE IF NOT EXISTS user_sessions
(
    user_session_id SERIAL PRIMARY KEY NOT NULL,
    user_id         UUID               NOT NULL,
    session_token   TEXT               NOT NULL UNIQUE,
    expires_at      TIMESTAMP          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_codes
(
    code_id    SERIAL PRIMARY KEY,
    user_id    UUID      NOT NULL,
    code       TEXT      NOT NULL,
    code_type  TEXT      NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS category
(
    category_id UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS i18n_category
(
    id          UUID PRIMARY KEY,
    category_id UUID REFERENCES category (category_id),
    language    TEXT NOT NULL,
    title       TEXT NOT NULL,
    slug        TEXT NOT NULL UNIQUE,
    description TEXT,
    CONSTRAINT unique_category_language UNIQUE (category_id, language)
);

CREATE TABLE IF NOT EXISTS articles
(
    article_id          UUID PRIMARY KEY,
    previous_article_id UUID REFERENCES articles (article_id),
    next_article_id     UUID REFERENCES articles (article_id),
    parent_article_id   UUID,
    category_id         UUID,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    position            INT,
    FOREIGN KEY (category_id) REFERENCES category (category_id),
    FOREIGN KEY (parent_article_id) REFERENCES articles (article_id)
);

CREATE TABLE IF NOT EXISTS i18n_articles
(
    id         UUID PRIMARY KEY,
    article_id UUID REFERENCES articles (article_id),
    language   TEXT NOT NULL,
    title      TEXT NOT NULL,
    slug       TEXT NOT NULL UNIQUE,
    content    TEXT NOT NULL,
    CONSTRAINT unique_article_language UNIQUE (article_id, language)
);

CREATE TABLE IF NOT EXISTS filter_test_table
(
    id               SERIAL PRIMARY KEY,
    string_field     TEXT NOT NULL,
    boolean_field    BOOLEAN,
    enum_theme_field TEXT,
    instant_field TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS filter_relation_test_table
(
    id                     SERIAL PRIMARY KEY,
    filter_test_entity_id  SERIAL  NOT NULL,
    string_relation_field  TEXT    NOT NULL,
    boolean_relation_field BOOLEAN NOT NULL,
    FOREIGN KEY (filter_test_entity_id) REFERENCES filter_test_table (id)
);

CREATE TABLE IF NOT EXISTS filter_one_to_one_relation_test_table
(
    id                     SERIAL PRIMARY KEY,
    filter_test_entity_id  SERIAL  NOT NULL,
    string_relation_field  TEXT    NOT NULL,
    boolean_relation_field BOOLEAN NOT NULL,
    FOREIGN KEY (filter_test_entity_id) REFERENCES filter_test_table (id)
);