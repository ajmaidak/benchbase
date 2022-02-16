DROP TABLE IF EXISTS shops;
CREATE TABLE shops (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255),
  settings VARCHAR(255),
  domain VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME
);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id BIGINT,
    shop_id BIGINT,
    customer_id BIGINT,
    is_deleted TINYINT(1),
    note VARCHAR(255),
    financial_status VARCHAR(255),
    updated_at DATETIME,
    created_at DATETIME,
    PRIMARY KEY (shop_id, id)
);

--   KEY `index_orders_on_customer_id` (`customer_id`,`id`),
--   KEY `index_orders_statuses` (`shop_id`,`closed_at`,`is_deleted`,`cancelled_at`,`financial_status`,`id`),