MERGE INTO users(name, email)
KEY(email)
VALUES('Михаил', 'test@test.ru'), ('Алексей', 'email@email.com'), ('Дмитрий', 'supertest@test.com');

MERGE INTO items(name, description, is_available, owner_id, request_id)
KEY(name)
VALUES('Дрель', 'Для сверления стен', true, 1, null), ('Пила', 'Для запила дверей', true, 1, null);

MERGE INTO bookings(start_date, end_date, item_id, booker_id, status)
KEY(item_id)
VALUES('2024-09-01', '2024-09-02', 1, 2, 'APPROVED'), ('2024-09-03', '2024-09-04', 1, 3, 'APPROVED');

MERGE INTO comments(text, item_id, author_id, created_at)
KEY(item_id)
VALUES('Достойно', 1, 2, '2024-09-03');

MERGE INTO requests(description, requester_id, created_at)
KEY(requester_id)
VALUES('Нужна болгарка', 3, '2024-09-04');