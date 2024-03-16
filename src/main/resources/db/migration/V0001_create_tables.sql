CREATE TABLE users (
     id UUID PRIMARY KEY,
     username TEXT NOT NULL UNIQUE,
     full_name TEXT NOT NULL,
     password VARCHAR(255) NOT NULL,
     picture TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
     updated_at TIMESTAMP
);

CREATE TABLE likes (
     id SERIAL PRIMARY KEY,
     user_from UUID NOT NULL,
     user_to UUID NOT NULL,
     value BOOLEAN NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
     CONSTRAINT user_likes_users_from_fk FOREIGN KEY (user_from) REFERENCES users (id) ON DELETE CASCADE,
     CONSTRAINT user_likes_users_to_fk FOREIGN KEY (user_to) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE chats (
     chat_id SERIAL NOT NULL,
     user_id UUID NOT NULL,
     PRIMARY KEY (chat_id, user_id),
     CONSTRAINT chat_user_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE messages (
     id SERIAL PRIMARY KEY,
     chat_id INT NOT NULL,
     user_from UUID NOT NULL,
     user_to UUID NOT NULL,
     content TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT messages_users_from_fk FOREIGN KEY (user_from) REFERENCES users (id) ON DELETE CASCADE,
     CONSTRAINT messages_users_to_fk FOREIGN KEY (user_to) REFERENCES users (id) ON DELETE CASCADE
);
