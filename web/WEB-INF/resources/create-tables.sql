BEGIN;

CREATE TABLE IF NOT EXISTS clients (id BIGSERIAL primary key, name VARCHAR(50), phone VARCHAR(20));
CREATE TABLE IF NOT EXISTS rooms (id BIGSERIAL primary key, number INTEGER);
CREATE TABLE IF NOT EXISTS bookings (client_id BIGINT, room_id BIGINT, foreign key (client_id) REFERENCES clients(id) ON DELETE CASCADE, foreign key (room_id) REFERENCES rooms(id) ON DELETE CASCADE);

COMMIT;