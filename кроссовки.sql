-- Таблица пользователей
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    phone VARCHAR(255),
    address TEXT,
    UNIQUE (id)
);

-- Таблица продавцов
CREATE TABLE sellers (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    UNIQUE (id)
);

-- Таблица категорий продуктов
CREATE TABLE categories (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    UNIQUE (id)
);

-- Таблица продуктов
CREATE TABLE products (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category INT NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    image VARCHAR(255),
    seller_id INT NOT NULL,
    FOREIGN KEY (category) REFERENCES categories(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES sellers(id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (id)
);

-- Таблица корзин (Bucket)
CREATE TABLE buckets (
    user_id VARCHAR(255),
    product_example_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (user_id, product_example_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (product_example_id) REFERENCES products_sizes(id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Таблица избранных товаров (Favorite)
CREATE TABLE favorites (
    user_id VARCHAR(255),
    product_id VARCHAR(255),
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Таблица магазинов (Store)
CREATE TABLE stores (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

-- Таблица заказов (Order)
CREATE TABLE orders (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    product_example_id INT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (product_example_id) REFERENCES products_sizes(id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (id)
);

-- Таблица соответствующих размеров (products_sizes)
CREATE TABLE products_sizes (
    id SERIAL PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    size_rus REAL NOT NULL,
    color VARCHAR(20) NOT NULL,
    image VARCHAR(255),
    store_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (store_id) REFERENCES stores(id) ON UPDATE CASCADE ON DELETE CASCADE
);
