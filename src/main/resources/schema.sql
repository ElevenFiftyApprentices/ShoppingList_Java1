CREATE SCHEMA IF NOT EXISTS shoppingList;
use shoppingList;

CREATE TABLE IF NOT EXISTS shoppingList.users (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  email VARCHAR(128) NOT NULL,
  password VARCHAR(200) NOT NULL,
  active tinyint(1) NOT NULL DEFAULT 1,
  role VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));
  
  CREATE TABLE IF NOT EXISTS shoppingList.lists (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  color VARCHAR(100) NOT NULL,
  created_utc
  modified_utc
  PRIMARY KEY(id));
  
CREATE TABLE IF NOT EXISTS shoppingList.listItems (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  shopping_list_id INT NOT NULL,
  contents VARCHAR(200) NOT NULL,
  priority INT NOT NULL,
  is_checked tinyint(1) NOT NULL DEFAULT 0,
  created_utc
  modified_utc
  PRIMARY KEY(id));
  
CREATE TABLE IF NOT EXISTS shoppingList.notess (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  shopping_list_item_id INT NOT NULL,
  body VARCHAR(200) NOT NULL,
  created_utc
  modified_utc
  PRIMARY KEY(id));