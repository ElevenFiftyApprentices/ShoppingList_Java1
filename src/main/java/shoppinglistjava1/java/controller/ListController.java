package shoppinglistjava1.java.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shoppinglistjava1.java.beans.ListItem;
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
		if (v.getShoppingLists().contains(l)) {
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

	@GetMapping("/list/{id}/additem")
	public String listItemAdd(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		model.addAttribute("list", shoppingListRepo.findOne(id));
		model.addAttribute("item", new ListItem());

		return "listitemadd";
	}

	@PostMapping("/list/{id}/additem")
	public String listItemAddSave(Model model, @PathVariable(name = "id") long id, @ModelAttribute @Valid ListItem item,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("item", item);
			return "listitemadd";
		} else {
			item.setList(shoppingListRepo.findOne(id));
			item.setCreatedUtc(new Date(System.currentTimeMillis()));
			item.setModifiedUtc(new Date(System.currentTimeMillis()));
			listItemRepo.save(item);
			return "redirect:/list/" + id;
		}
	}
	
	@GetMapping("/addlist")
	public String listAdd(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		model.addAttribute("list", new ShoppingList());
		return "addlist";
	}

	@PostMapping("/addlist")
	public String listAddSave(Model model, @ModelAttribute @Valid ShoppingList list,
			BindingResult result) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		if (result.hasErrors()) {
			model.addAttribute("list", list);
			return "addlist";
		} else {
			list.setUser(v);
			list.setCreatedUTC(new Date(System.currentTimeMillis()));
			list.setModifiedUTC(new Date(System.currentTimeMillis()));
			shoppingListRepo.save(list);
			return "redirect:/lists/";
		}
	}
}
