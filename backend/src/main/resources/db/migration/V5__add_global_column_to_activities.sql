ALTER TABLE activities ADD COLUMN global BOOLEAN NOT NULL DEFAULT false;
UPDATE activities SET global = true WHERE user_id IS NULL;