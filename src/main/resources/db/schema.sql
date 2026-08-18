-- CloudOps PSA — PostgreSQL Schema DDL (Client & Billing Service)
-- Native Local & Cloud SQL PostgreSQL 16 Script

-- 1. Create clients table
CREATE TABLE IF NOT EXISTS clients (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_name    VARCHAR(200)  NOT NULL,
    contact_email   VARCHAR(150)  NOT NULL UNIQUE,
    contact_phone   VARCHAR(30),
    address         TEXT,
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- 2. Create subscriptions table
CREATE TABLE IF NOT EXISTS subscriptions (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id         UUID          NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    product_id        VARCHAR(50)   NOT NULL,
    plan_tier         VARCHAR(30)   NOT NULL,
    monthly_amount    DECIMAL(10,2) NOT NULL,
    billing_cycle     VARCHAR(20)   NOT NULL DEFAULT 'MONTHLY',
    start_date        DATE          NOT NULL,
    next_billing_date DATE          NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    created_at        TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_subscriptions_client_id ON subscriptions(client_id);

-- 3. Create invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id         UUID          NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    subscription_id   UUID          REFERENCES subscriptions(id),
    invoice_number    VARCHAR(30)   NOT NULL UNIQUE,
    amount_due        DECIMAL(10,2) NOT NULL,
    tax_amount        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount      DECIMAL(10,2) NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'UNPAID',
    issue_date        DATE          NOT NULL,
    due_date          DATE          NOT NULL,
    gcs_pdf_url       VARCHAR(500),
    created_at        TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_invoices_client_id ON invoices(client_id);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(status);

-- 4. Create payments table
CREATE TABLE IF NOT EXISTS payments (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_id              UUID          NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    amount_paid             DECIMAL(10,2) NOT NULL,
    payment_method          VARCHAR(30)   NOT NULL,
    transaction_reference   VARCHAR(100),
    status                  VARCHAR(20)   NOT NULL DEFAULT 'COMPLETED',
    payment_date            TIMESTAMP     NOT NULL,
    created_at              TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_payments_invoice_id ON payments(invoice_id);
