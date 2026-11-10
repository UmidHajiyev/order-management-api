CREATE TABLE products (
    id          BIGSERIAL    PRIMARY KEY,
    sku         VARCHAR(64)  NOT NULL UNIQUE,
    name        VARCHAR(200) NOT NULL,
    price_cents BIGINT       NOT NULL CHECK (price_cents >= 0),
    stock       INTEGER      NOT NULL CHECK (stock >= 0),
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL
);

CREATE TABLE orders (
    id              BIGSERIAL    PRIMARY KEY,
    idempotency_key VARCHAR(100) UNIQUE,
    customer_email  VARCHAR(254) NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    total_cents     BIGINT       NOT NULL CHECK (total_cents >= 0),
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_orders_status_created_at ON orders (status, created_at);

CREATE TABLE order_lines (
    id               BIGSERIAL    PRIMARY KEY,
    order_id         BIGINT       NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id       BIGINT       NOT NULL REFERENCES products (id),
    sku              VARCHAR(64)  NOT NULL,
    product_name     VARCHAR(200) NOT NULL,
    unit_price_cents BIGINT       NOT NULL CHECK (unit_price_cents >= 0),
    quantity         INTEGER      NOT NULL CHECK (quantity > 0),
    line_total_cents BIGINT       NOT NULL CHECK (line_total_cents >= 0)
);

CREATE INDEX idx_order_lines_order_id ON order_lines (order_id);
CREATE INDEX idx_order_lines_product_id ON order_lines (product_id);
