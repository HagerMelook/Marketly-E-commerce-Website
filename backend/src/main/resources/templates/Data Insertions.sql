-- Insert data into account
INSERT INTO account (auth_type, status, type, username) VALUES
                                                            ('OAuth', 1, 'Admin', 'admin1'),
                                                            ('OAuth', 1, 'Admin', 'admin2'),
                                                            ('Basic', 1, 'Client', 'client1'),
                                                            ('Basic', 1, 'Client', 'client2'),
                                                            ('Basic', 1, 'Vendor', 'vendor1'),
                                                            ('Basic', 1, 'Vendor', 'vendor2'),
                                                            ('OAuth', 0, 'Client', 'client3'),
                                                            ('OAuth', 1, 'Client', 'client4'),
                                                            ('Basic', 1, 'Admin', 'admin3'),
                                                            ('OAuth', 1, 'Vendor', 'vendor3');

-- Insert data into admin
INSERT INTO admin (account_id, first_name, last_name) VALUES
                                                          (1, 'John', 'Doe'),
                                                          (2, 'Jane', 'Smith'),
                                                          (9, 'Alice', 'Johnson');

-- Insert data into client
INSERT INTO client (account_id, first_name, last_name) VALUES
                                                           (3, 'Michael', 'Brown'),
                                                           (4, 'Emily', 'White'),
                                                           (7, 'Daniel', 'King'),
                                                           (8, 'Sophia', 'Green');

-- Insert data into vendor
INSERT INTO vendor (account_id, organization_name, tax_number) VALUES
                                                                   (5, 'TechCorp', 123456789),
                                                                   (6, 'SmartInc', 987654321),
                                                                   (10, 'Bright Solutions', 567890123);

-- Insert data into product
INSERT INTO product (product_category, product_description, product_name, product_price, product_quantity, product_rating, vendor_id) VALUES
                                                                                                                                          ('Electronics', 'Smartphone with 128GB storage', 'PhoneA', 699.99, 50, 4.5, 5),
                                                                                                                                          ('Electronics', '4K Ultra HD TV', 'TVX1000', 1199.99, 20, 4.7, 5),
                                                                                                                                          ('Home Appliances', 'Automatic Washing Machine', 'WashPro', 799.99, 10, 4.2, 6),
                                                                                                                                          ('Books', 'Bestselling Fiction Novel', 'BookA', 19.99, 100, 4.8, 6),
                                                                                                                                          ('Toys', 'Building Blocks Set', 'BlocksMax', 29.99, 200, 4.3, 6),
                                                                                                                                          ('Electronics', 'Noise-Cancelling Headphones', 'HeadsetX', 149.99, 30, 4.6, 10),
                                                                                                                                          ('Fashion', 'Leather Jacket', 'JacketZ', 199.99, 15, 4.4, 10),
                                                                                                                                          ('Sports', 'Mountain Bike', 'BikeY', 999.99, 5, 4.9, 10),
                                                                                                                                          ('Electronics', 'Laptop with 16GB RAM', 'LaptopPro', 1299.99, 25, 4.8, 10),
                                                                                                                                          ('Fashion', 'Designer Sunglasses', 'ShadesX', 89.99, 40, 4.3, 10);

-- Insert data into cart_products
INSERT INTO cart_products (account_id, product_id, products_count) VALUES
                                                                       (3, 1, 2),
                                                                       (3, 4, 1),
                                                                       (4, 2, 1),
                                                                       (4, 3, 1),
                                                                       (4, 8, 1),
                                                                       (7, 6, 3),
                                                                       (8, 5, 1),
                                                                       (8, 9, 2),
                                                                       (7, 10, 1),
                                                                       (8, 7, 2);

-- Insert data into category
INSERT INTO category (category_name, category_image_path) VALUES
                                                              ('Electronics', '/images/electronics.jpg'),
                                                              ('Home Appliances', '/images/home_appliances.jpg'),
                                                              ('Books', '/images/books.jpg'),
                                                              ('Toys', '/images/toys.jpg'),
                                                              ('Fashion', '/images/fashion.jpg'),
                                                              ('Sports', '/images/sports.jpg');

-- Insert data into orders
INSERT INTO orders (order_checkout_price, order_date, order_status, account_id) VALUES
                                                                                    (1399.98, '2024-12-10', 'Completed', 3),
                                                                                    (29.99, '2024-12-12', 'Pending', 4),
                                                                                    (149.99, '2024-12-13', 'Shipped', 7),
                                                                                    (999.99, '2024-12-11', 'Completed', 8),
                                                                                    (89.99, '2024-12-13', 'Pending', 7);

-- Insert data into order_products
INSERT INTO order_products (order_id, product_id) VALUES
                                                      (1, 1),
                                                      (1, 2),
                                                      (2, 5),
                                                      (3, 6),
                                                      (4, 8),
                                                      (5, 10);

-- Insert data into comment
INSERT INTO comment (comment_body, account_id, product_id) VALUES
                                                               ('Amazing quality!', 3, 1),
                                                               ('Not worth the price.', 4, 2),
                                                               ('Highly recommended!', 7, 8),
                                                               ('Good product.', 8, 5),
                                                               ('Would buy again.', 7, 10);

-- Insert data into feedback
INSERT INTO feedback (feedback_body) VALUES
                                         ('Great service!'),
                                         ('User-friendly platform.'),
                                         ('Delivery was delayed.'),
                                         ('Products are high quality.'),
                                         ('Prices could be better.');

-- Insert data into product_image
INSERT INTO product_image (image_path, product_id) VALUES
                                                       ('/images/phonea.jpg', 1),
                                                       ('/images/tvx1000.jpg', 2),
                                                       ('/images/washpro.jpg', 3),
                                                       ('/images/booka.jpg', 4),
                                                       ('/images/blocksmax.jpg', 5),
                                                       ('/images/headsetx.jpg', 6),
                                                       ('/images/jacketz.jpg', 7),
                                                       ('/images/bikey.jpg', 8),
                                                       ('/images/laptoppro.jpg', 9),
                                                       ('/images/shadesx.jpg', 10);

-- Insert data into shipping_info
INSERT INTO shipping_info (account_id, address, phone_number, postal_code) VALUES
                                                                               (3, '123 Elm Street', '1234567890', '10001'),
                                                                               (4, '456 Oak Avenue', '9876543210', '20002'),
                                                                               (7, '789 Pine Drive', '5555555555', '30003'),
                                                                               (8, '321 Maple Lane', '1112223333', '40004');

-- Insert data into shopping_cart
INSERT INTO shopping_cart (account_id, total_cost) VALUES
                                                       (3, 720.97),
                                                       (4, 30.99),
                                                       (7, 1149.97),
                                                       (8, 1179.97);

-- Insert data into vendor_request
INSERT INTO vendor_request (organization_name, password, tax_number, username) VALUES
                                                                                   ('FutureTech', 'securePass1', 102030405, 'future_tech'),
                                                                                   ('BrightLabs', 'securePass2', 506070809, 'bright_labs'),
                                                                                   ('InnoGadget', 'securePass3', 304050607, 'inno_gadget');

