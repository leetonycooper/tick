CREATE TABLE symbol (
  instrument_id INT(11) NOT NULL,
  trade_pair VARCHAR(10) NULL DEFAULT NULL,
  exchange_name VARCHAR(50) NULL DEFAULT NULL,
  feed_group_id INT(11) NOT NULL,
  PRIMARY KEY (instrument_id)
);