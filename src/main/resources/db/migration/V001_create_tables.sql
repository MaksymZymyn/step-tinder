CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username TEXT NOT NULL UNIQUE,
                       full_name TEXT NOT NULL,
                       picture TEXT,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE likes (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_from UUID NOT NULL,
                       user_to UUID NOT NULL,
                       value BOOLEAN NOT NULL,
                       CONSTRAINT user_likes_users_from_fk FOREIGN KEY (user_from) REFERENCES users (id) ON DELETE CASCADE,
                       CONSTRAINT user_likes_users_to_fk FOREIGN KEY (user_to) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE messages (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_from UUID NOT NULL,
                          user_to UUID NOT NULL,
                          content TEXT,
                          time TIMESTAMP,
                          CONSTRAINT messages_users_from_fk FOREIGN KEY (user_from) REFERENCES users (id) ON DELETE CASCADE,
                          CONSTRAINT messages_users_to_fk FOREIGN KEY (user_to) REFERENCES users (id) ON DELETE CASCADE
);
