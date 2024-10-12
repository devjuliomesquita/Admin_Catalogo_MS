create table tb_categories (
    category_id varchar(36) not null primary key,
    category_name varchar(255) not null,
    category_description varchar(4000),
    category_active boolean not null default true,
    category_created_at datetime(6) not null,
    category_updated_at datetime(6) not null,
    category_deleted_at datetime(6))