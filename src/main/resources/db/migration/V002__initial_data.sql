INSERT INTO users (username, password, email, avatar) VALUES
      ('John', '12345', 'john@gmail.com', 'https://robohash.org/68.186.255.198.png'),
      ('Mary', '11111', 'mary@gmail.com', 'https://robohash.org/548.png?set=set4'),
      ('Tom', 'qwert', 'tom@gmail.com', 'https://robohash.org/77.123.13.90.png'),
      ('Anna', '22222', 'anna@gmail.com', 'https://robohash.org/7PD.png?set=set4'),
      ('Alex', '43212', 'alex@gmail.com', 'https://robohash.org/12Z.png?set=set3'),
      ('Don', '22ss222', 'don@gmail.com', 'https://robohash.org/SS0.png?set=set2'),
      ('Nora', 'dffe55', 'nora@gmail.com', 'https://robohash.org/591.png?set=set4'),
      ('Kevin', '2ddg222', 'kevin@gmail.com', 'https://robohash.org/tkitu'),
      ('Kity', '840629', 'kity@gmail.com', 'https://robohash.org/kitys'),
      ('Harry', 'trewq', 'harry@gmail.com', 'https://robohash.org/Y0C.png?set=set4');

-- Inserting data into the 'user_likes' table
INSERT INTO user_likes (user_from, user_to, value) VALUES
      (1, 2, true),
      (1, 3, false),
      (2, 1, true),
      (2, 3, true),
      (3, 1, false),
      (3, 2, false);

-- Inserting data into the 'chat_user' table
INSERT INTO chat_user (chat_id, user_id) VALUES
       (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 3);

INSERT INTO messages (user_from, user_to, content, created_at) VALUES
       (4, 1, 'Buy', '2023-05-08 17:46:20.000000'),
       (1, 4, 'How are you ?', '2023-05-08 17:43:58.000000'),
       (4, 1, 'nice. Are you fine ?', '2023-05-08 17:45:16.000000'),
       (1, 4, 'Yes always', '2023-05-08 17:45:40.000000'),
       (4, 1, 'And you?', '2023-05-08 17:45:55.000000'),
       (4, 1, 'Buy', '2023-05-08 17:45:57.000000'),
       (4, 1, 'Hello\n How are you?', '2023-05-08 17:46:08.000000'),
       (4, 1, 'Fine', '2023-05-08 17:46:12.000000'),
       (4, 1, 'Hi', '2023-05-08 17:46:16.000000'),
       (4, 1, 'Buy', '2023-05-08 17:46:20.000000'),
       (4, 2, 'Hi', '2023-05-01 12:48:51.000000'),
       (2, 4, 'Hello', '2023-05-04 17:49:05.000000'),
       (4, 3, 'Hello\n How are you?', '2023-05-07 17:49:15.000000'),
       (3, 4, 'Hello', '2023-05-08 09:01:26.000000'),
       (3, 4, 'Fine', '2023-05-08 09:02:26.000000'),
       (3, 4, 'And you?', '2023-05-08 09:01:26.000000');
