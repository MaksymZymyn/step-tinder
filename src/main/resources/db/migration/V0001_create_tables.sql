CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username TEXT NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       avatar TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE user_likes (
                            id SERIAL PRIMARY KEY,
                            user_from INT NOT NULL,
                            user_to INT NOT NULL,
                            value BOOLEAN NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            CONSTRAINT user_likes_users_from_fk FOREIGN KEY (user_from) REFERENCES users (id) ON DELETE CASCADE,
                            CONSTRAINT user_likes_users_to_fk FOREIGN KEY (user_to) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE chat_user (
                           chat_id INT NOT NULL,
                           user_id INT NOT NULL,
                           PRIMARY KEY (chat_id, user_id),
                           CONSTRAINT chat_user_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,
                          chat INT NOT NULL,
                          user_from INT NOT NULL,
                          user_to INT NOT NULL,
                          content TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT messages_users_id_fk FOREIGN KEY (user_from) REFERENCES users (id) ON DELETE CASCADE
);
