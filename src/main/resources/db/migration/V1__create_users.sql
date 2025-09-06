CREATE TABLE IF NOT EXISTS app_user
(
    id              UUID            PRIMARY KEY,
    username        VARCHAR(100)    NOT NULL UNIQUE,
    password_hash   VARCHAR(200)    NOT NULL,
    avatar_url      VARCHAR(512),
    roles           TEXT[]          NOT NULL DEFAULT ARRAY ['USER']
);

CREATE INDEX IF NOT EXISTS idx_app_user_username ON app_user (username);
