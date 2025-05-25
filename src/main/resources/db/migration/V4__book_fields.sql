ALTER TABLE books
ADD COLUMN key VARCHAR(255),
ADD COLUMN subtitle VARCHAR(255),
ADD COLUMN isbn10 TEXT[],
ADD COLUMN page_number SMALLINT,
ADD COLUMN publish_date VARCHAR(255),
ADD COLUMN publish_country VARCHAR(255),
ADD COLUMN publish_places TEXT[],
ADD COLUMN publishers TEXT[],
ADD COLUMN subjects TEXT[],
ADD COLUMN contributions TEXT[];