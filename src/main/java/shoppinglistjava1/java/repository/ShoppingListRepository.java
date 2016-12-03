package shoppinglistjava1.java.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shoppinglistjava1.java.beans.ShoppingList;
import shoppinglistjava1.java.beans.User;


@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long>{
	List<ShoppingList> findAllByUser(User user);
	List<ShoppingList> findAllByUserOrderByCategoryAsc(User user);
	List<ShoppingList> findAllByUserOrderByCategoryDesc(User user);
	List<ShoppingList> findAllByUserOrderByNameAsc(User user);
	List<ShoppingList> findAllByUserOrderByNameDesc(User user);
	List<ShoppingList> findByCategoryContainsOrNameContainsAllIgnoreCase(String categoryPart, String namePart);
}
