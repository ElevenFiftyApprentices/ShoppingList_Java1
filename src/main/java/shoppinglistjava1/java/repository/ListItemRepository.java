package shoppinglistjava1.java.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shoppinglistjava1.java.beans.ListItem;
import shoppinglistjava1.java.beans.ShoppingList;

@Repository
public interface ListItemRepository extends CrudRepository<ListItem, Long> {

	List<ListItem> findByListOrderByPriorityAsc(ShoppingList list);
	List<ListItem> findByListOrderByPriorityDesc(ShoppingList list);
	List<ListItem> findByListOrderByContentsAsc(ShoppingList list);
	List<ListItem> findByListOrderByContentsDesc(ShoppingList list);
<<<<<<< HEAD
	List<ListItem> findByContentsContainsOrNoteContainsAllIgnoreCase(String contentsPart, String notePart);
=======
	List<ListItem> findByListAndContentsContainsOrPriorityContainsAllIgnoreCase(ShoppingList list, String contentsPart, String priorityPart);
>>>>>>> 299ceae29c79b59ea7e834528cef658827b565e5
	
}
