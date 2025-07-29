DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status') THEN
        CREATE TYPE status_type AS enum (
                'PROCESSING',
                'SUCCESS',
                'FAILURE_IN_PAYMENT_INFORMATION'
        );
    END IF;
END $$;

CREATE TABLE "payment"(
    id UUID primary key,
    total_value NUMERIC(19,2) NOT NULL,
    status status_type NOT NULL,
    credit_card_number VARCHAR(20) NOT NULL,
    message VARCHAR
);