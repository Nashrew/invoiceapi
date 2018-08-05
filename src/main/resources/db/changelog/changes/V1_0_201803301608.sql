CREATE TABLE IF NOT EXISTS invoices (
    id IDENTITY,
    invoice_number VARCHAR(64) NOT NULL,
    po_number VARCHAR(64) NOT NULL,
    due_date DATE NULL,
    amount_cents BIGINT NOT NULL,
    created_at TIMESTAMP
);