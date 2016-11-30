insert into shoppingList.users (first_name, last_name, password, email, role, active) values ('Tester', 'tester', '$2a$10$RwdkOUIJCUjpToGUKSM.OeMBpNw87Gz2V1qh/jqJC2IAGAd/dlbP.', 'cchtesting27@gmail.com', 'USER', true);

insert into shoppingList.lists (user_id, name, color, created_utc, modified_utc) values (1, 'Test List', '#000000', '2016-11-30 02:35:55', '2016-11-30 02:35:55');
insert into shoppingList.lists (user_id, name, color, created_utc, modified_utc) values (1, 'Test List 2', '#000000', '2016-11-30 02:35:55', '2016-11-30 02:35:55');
insert into shoppingList.lists (user_id, name, color, created_utc, modified_utc) values (1, 'Test List 3', '#000000', '2016-11-30 02:35:55', '2016-11-30 02:35:55');

insert into shoppingList.list_items (shopping_list_id, contents, priority, is_checked, created_utc, modified_utc) values (1, 'cat food', 1, false, '2016-11-30 02:35:55', '2016-11-30 02:35:55');
insert into shoppingList.list_items (shopping_list_id, contents, priority, is_checked, created_utc, modified_utc) values (1, 'cat toy', 1, false, '2016-11-30 02:35:55', '2016-11-30 02:35:55');
insert into shoppingList.list_items (shopping_list_id, contents, priority, is_checked, created_utc, modified_utc) values (1, 'cat litter', 1, false, '2016-11-30 02:35:55', '2016-11-30 02:35:55');