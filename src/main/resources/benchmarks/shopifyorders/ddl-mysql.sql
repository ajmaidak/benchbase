SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS shops;
CREATE TABLE shops (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL DEFAULT '',
  settings mediumtext,
  domain varchar(255) DEFAULT NULL,
  created_at datetime(6) DEFAULT NULL,
  updated_at datetime(6) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY index_shops_on_updated_at (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    shop_id bigint(20) NOT NULL,
    customer_id bigint(20) NOT NULL,
    is_deleted tinyint(1) NOT NULL DEFAULT '0',
    note mediumtext,
    financial_status varchar(255) DEFAULT NULL,
    updated_at datetime(6) DEFAULT NULL,
    created_at datetime(6) DEFAULT NULL,
    PRIMARY KEY (shop_id, id),
    KEY index_orders_on_customer_id (customer_id, id),
    KEY index_orders_statuses (shop_id, is_deleted, financial_status, id),
    KEY id (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;