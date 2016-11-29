package shoppinglistjava1.java.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shoppinglistjava1.java.beans.ShoppingList;


@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long>{

}
