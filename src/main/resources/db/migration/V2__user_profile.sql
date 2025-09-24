CREATE TABLE profile_user
(
    login_user_id UUID              PRIMARY KEY REFERENCES login_user (id) ON DELETE CASCADE,
    avatar_url    TEXT,
    display_name  VARCHAR(100),
    created_at    TIMESTAMP         NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP         NOT NULL DEFAULT now()
);

CREATE TRIGGER update_profile_user_updated_at
    BEFORE UPDATE ON profile_user
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();