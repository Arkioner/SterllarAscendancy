CREATE TABLE IF NOT EXISTS login_user
(
    id              UUID            PRIMARY KEY,
    username        VARCHAR(100)    NOT NULL UNIQUE,
    password_hash   VARCHAR(200)    NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT now()
);

CREATE TRIGGER update_login_user_updated_at
    BEFORE UPDATE ON login_user
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();