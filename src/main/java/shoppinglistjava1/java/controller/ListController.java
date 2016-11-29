package shoppinglistjava1.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import shoppinglistjava1.java.beans.ShoppingList;
import shoppinglistjava1.java.beans.User;
import shoppinglistjava1.java.repository.ListItemRepository;
import shoppinglistjava1.java.repository.NoteRepository;
import shoppinglistjava1.java.repository.ShoppingListRepository;
import shoppinglistjava1.java.repository.UserRepository;

@Controller
public class ListController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ListItemRepository listItemRepo;
	
	@Autowired
	private ShoppingListRepository shoppingListRepo;
	
	@Autowired
	private NoteRepository noteRepo;
	
	
	@GetMapping("/list/{id}")
	public String list(Model model, @PathVariable(name = "id") long id) {
		model.addAttribute("id", id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		ShoppingList l = shoppingListRepo.findOne(id);
		if(v.getShoppingLists().contains(l)){
		model.addAttribute("shoppingList", l);
		}
		return "listview";
	}
	
	@GetMapping("/lists")
	public String lists(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		model.addAttribute("lists", shoppingListRepo.findAllByUser(v));
		return "lists";
	}
	
}
