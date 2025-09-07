CREATE TABLE user_profile (
      user_id       UUID            PRIMARY KEY REFERENCES app_user(id) ON DELETE CASCADE,
      avatar_url    TEXT,
      display_name  VARCHAR(100),
      bio           TEXT,
      updated_at    TIMESTAMP       DEFAULT now()
);