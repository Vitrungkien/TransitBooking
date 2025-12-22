alter table notice_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table notice_tb
    modify last_update DATETIME DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP;

alter table stop_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table order_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table order_tb
    modify last_update DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;

alter table product_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table product_tb
    modify last_update DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;

alter table user_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table user_tb
    add last_update DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;

alter table stop_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table stop_tb
    add last_update DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;

alter table store_tb
    modify created_at DATETIME DEFAULT CURRENT_TIMESTAMP;

alter table store_tb
    add last_update DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;