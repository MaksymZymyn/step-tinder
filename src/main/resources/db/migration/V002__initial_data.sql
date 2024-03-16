CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Inserting data into the 'users' table
INSERT INTO users (id, username, full_name, picture, password) VALUES
     (uuid_generate_v4(), 'John', 'John Doe', 'https://robohash.org/68.186.255.198.png', '12345'),
     (uuid_generate_v4(), 'Mary', 'Mary Smith', 'https://robohash.org/548.png?set=set4', '11111'),
     (uuid_generate_v4(), 'Tom', 'Tom Johnson', 'https://robohash.org/77.123.13.90.png', 'qwert'),
     (uuid_generate_v4(), 'Anna', 'Anna Lee', 'https://robohash.org/7PD.png?set=set4', '22222'),
     (uuid_generate_v4(), 'Alex', 'Alex Brown', 'https://robohash.org/12Z.png?set=set3', '43212'),
     (uuid_generate_v4(), 'Don', 'Don Williams', 'https://robohash.org/SS0.png?set=set2', '22ss222'),
     (uuid_generate_v4(), 'Nora', 'Nora Miller', 'https://robohash.org/591.png?set=set4', 'dffe55'),
     (uuid_generate_v4(), 'Kevin', 'Kevin Davis', 'https://robohash.org/tkitu', '2ddg222'),
     (uuid_generate_v4(), 'Kity', 'Kity Anderson', 'https://robohash.org/kitys', '840629'),
     (uuid_generate_v4(), 'Harry', 'Harry Wilson', 'https://robohash.org/Y0C.png?set=set4', 'trewq');
