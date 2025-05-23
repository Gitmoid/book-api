ALTER TABLE authors
ALTER COLUMN birth_year TYPE VARCHAR(255);

ALTER TABLE authors
RENAME COLUMN birth_year TO birth_date;