package shoppinglistjava1.java.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shoppinglistjava1.java.beans.ListItem;
import shoppinglistjava1.java.beans.Note;
import shoppinglistjava1.java.beans.ShoppingList;
import shoppinglistjava1.java.beans.User;
import shoppinglistjava1.java.repository.ListItemRepository;
import shoppinglistjava1.java.repository.NoteRepository;
import shoppinglistjava1.java.repository.ShoppingListRepository;
import shoppinglistjava1.java.repository.UserRepository;

@Controller
public class ListController {

	private static UserRepository userRepo;

	@Autowired
	private UserRepository userrRepo;

	@PostConstruct
	public void initStaticUserRepo() {
		userRepo = this.userrRepo;
	}

	@Autowired
	private ListItemRepository listItemRepo;

	@Autowired
	private ShoppingListRepository shoppingListRepo;

	@Autowired
	private NoteRepository noteRepo;

	@GetMapping("/list/{id}")
	public String list(Model model, @PathVariable(name = "id") long id,
			@RequestParam(name = "srch-term", required = false) String searchTerm) {
		model.addAttribute("id", id);
		User currentUser = ListController.getCurrentUser();
		ShoppingList l = shoppingListRepo.findOne(id);
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		if (currentUser.getShoppingLists().contains(l)) {
			model.addAttribute("shoppingList", l);
		}
		if (searchTerm == null || "".equals(searchTerm)) {
			model.addAttribute("listItems", l.getListItems());
		} else {
			List<ListItem> li = listItemRepo.findByListAndContentsContainsOrPriorityContainsAllIgnoreCase(l, searchTerm, searchTerm);
			ArrayList<ListItem> li2 = new ArrayList();
			for (ListItem lix : li) {
				if (l.getListItems().contains(lix)) {
					li2.add(lix);
				}
			}
			model.addAttribute("listItems", li2);
		}
		return "listview";
		}
	}

	@GetMapping("/lists")
	public String lists(Model model, @RequestParam(name = "srch-term", required = false) String searchTerm) {
		User currentUser = ListController.getCurrentUser();
		if (searchTerm == null || "".equals(searchTerm)) {
			model.addAttribute("lists", shoppingListRepo.findAllByUser(currentUser));
		} else {
			ArrayList<ShoppingList> userLists = new ArrayList<ShoppingList>();
			List<ShoppingList> lists = shoppingListRepo.findByCategoryContainsOrNameContainsAllIgnoreCase(searchTerm,
					searchTerm);
			for (ShoppingList list : lists) {
				if (list.getUser() == currentUser) {
					userLists.add(list);
				}
			}
			model.addAttribute("lists", userLists);
		}
		return "lists";
	}

	@GetMapping("/list/{id}/additem")
	public String listItemAdd(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		model.addAttribute("list", shoppingListRepo.findOne(id));
		Note n = new Note();
		ListItem li = new ListItem();
		li.setNote(n);
		model.addAttribute("note", n);
		model.addAttribute("item", li);
		return "listitemadd";
		}
	}

	@PostMapping("/list/{id}/additem")
	public String listItemAddSave(Model model, @PathVariable(name = "id") long id, @ModelAttribute @Valid ListItem item,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("list", shoppingListRepo.findOne(id));
			model.addAttribute("item", item);
			return "listitemadd";
		} else {
			model.addAttribute("list", shoppingListRepo.findOne(id));
			item.setList(shoppingListRepo.findOne(id));
			item.setCreatedUtc();
			item.setModifiedUtc();
			Note n = new Note();
			n.setBody(" ");
			noteRepo.save(n);
			System.out.println(n.getId());
			item.setNote(n);
			listItemRepo.save(item);
			model.addAttribute("listItems", shoppingListRepo.findOne(id).getListItems());
			return "redirect:/list/" + id;
		}
	}

	@GetMapping("/list/{id}/edititem/{itemid}")
	public String listItemEdit(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		model.addAttribute("list", shoppingListRepo.findOne(id));
		model.addAttribute("item", listItemRepo.findOne(itemid));
		return "listitemedit";
		}
	}

	@PostMapping("/list/{id}/edititem/{itemid}")
	public String listItemEditSave(Model model, @PathVariable(name = "id") long id,
			@ModelAttribute @Valid ListItem item, @PathVariable(name = "itemid") long itemid, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("item", item);
			return "listitemedit";
		} else {
			item.setList(shoppingListRepo.findOne(id));
			item.setModifiedUtc();
			item.setNote(listItemRepo.findOne(itemid).getNote());
			listItemRepo.save(item);
			model.addAttribute("listItems", shoppingListRepo.findOne(id).getListItems());
			return "redirect:/list/" + id;
		}
	}

	@GetMapping("/addlist")
	public String listAdd(Model model) {
		model.addAttribute("list", new ShoppingList());
		return "addlist";
	}

	@PostMapping("/addlist")
	public String listAddSave(Model model, @ModelAttribute @Valid ShoppingList list, BindingResult result) {
		User currentUser = ListController.getCurrentUser();
		if (result.hasErrors()) {
			model.addAttribute("list", list);
			return "addlist";
		} else {
			list.setUser(currentUser);
			list.setCreatedUTC();
			list.setModifiedUTC();
			shoppingListRepo.save(list);
			return "redirect:/lists/";
		}
	}

	@GetMapping("/list/{id}/delete/{itemid}")
	public String listItemDelete(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		model.addAttribute("list", shoppingListRepo.findOne(id));
		model.addAttribute("item", listItemRepo.findOne(itemid));
		return "listitemdelete";
		}
	}

	@PostMapping("/list/{id}/delete/{itemid}")
	public String listItemDeleteSave(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = l.getListItems();
		noteRepo.delete(listItemRepo.findOne(itemid).getNote());
		listItemRepo.delete(listItemRepo.findOne(itemid));
		li.remove(listItemRepo.findOne(itemid));
		model.addAttribute("list", l);
		return "redirect:/list/" + id;
	}

	@GetMapping("/list/{id}/delete")
	public String listDelete(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		model.addAttribute("list", shoppingListRepo.findOne(id));
		return "deletelist";
		}
	}

	@PostMapping("/list/{id}/delete")
	public String listDeleteSave(Model model, @PathVariable(name = "id") long id,
			@ModelAttribute @Valid ShoppingList list, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("list", list);
			return "deletelist";
		} else {
			for(ListItem item : shoppingListRepo.findOne(id).getListItems()){
			listItemRepo.delete(item); 
			}
			shoppingListRepo.delete(list);
			model.addAttribute("lists", shoppingListRepo.findAll());
			return "redirect:/lists";
		}
	}

	@GetMapping("/list/{id}/check/{itemid}")
	public String listItemCheck(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ListItem i = listItemRepo.findOne(itemid);
		i.setChecked(true);
		listItemRepo.save(i);
		model.addAttribute("shoppingList", shoppingListRepo.findOne(id));
		model.addAttribute("listItems", shoppingListRepo.findOne(id).getListItems());
		return "listview";
		}
	}

	@GetMapping("/list/{id}/clearchecked")
	public String clearChecked(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = l.getListItems();
		for (ListItem i : li) {
			i.setChecked(false);
			listItemRepo.save(i);
		}
		shoppingListRepo.save(l);
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", shoppingListRepo.findOne(id));
		return "listview";
		}
	}

	@GetMapping("/list/{id}/deletechecked")
	public String deleteChecked(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		model.addAttribute("list", l);
		model.addAttribute("listItems", l.getListItems());
		return "deletechecked";
		}
	}

	@PostMapping("/list/{id}/deletechecked")
	public String deleteCheckedSave(Model model, @PathVariable(name = "id") long id) {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = l.getListItems();
		ArrayList<ListItem> liTwo = new ArrayList<ListItem>();
		for (ListItem i : li) {
			if (i.isChecked == true) {
				liTwo.add(i);
			}
		}
		for (ListItem i2 : liTwo) {
			li.remove(i2);
			listItemRepo.delete(i2);
		}

		l.setListItems(li);
		shoppingListRepo.save(l);
		model.addAttribute("list", l);
		return "redirect:/list/" + id;
	}

	@GetMapping("/list/{id}/high")
	public String orderHigh(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByPriorityAsc(l);
		for (ListItem lix : li) {
		}
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
		}
	}

	@GetMapping("/list/{id}/low")
	public String orderLow(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByPriorityDesc(l);
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
		}
	}

	@GetMapping("/list/{id}/az")
	public String orderAz(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByContentsAsc(l);
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
		}
	}

	@GetMapping("/list/{id}/za")
	public String orderZa(Model model, @PathVariable(name = "id") long id) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByContentsDesc(l);
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
		}
	}

	@GetMapping("/list/{id}/edititem/{itemid}/editnote")
	public String NoteEdit(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		User currentUser = ListController.getCurrentUser();
		if(currentUser.equals(shoppingListRepo.findOne(id).getUser())){
			return "redirect:/lists";
		} else {
		ShoppingList l = shoppingListRepo.findOne(id);
		ListItem li = listItemRepo.findOne(itemid);
		if (listItemRepo.findOne(itemid).getNote() == null) {
			Note n = new Note();
			li.setNote(n);
			model.addAttribute("note", n);
		} else {
			Note n = listItemRepo.findOne(itemid).getNote();
			li.setNote(n);
			model.addAttribute("note", n);
		}
		model.addAttribute("list", l);
		model.addAttribute("item", li);
		return "noteedit";
		}
	}

	@PostMapping("/list/{id}/edititem/{itemid}/editnote")
	public String NoteEditSave(Model model, @PathVariable(name = "id") long id, @ModelAttribute @Valid Note note,
			@PathVariable(name = "itemid") long itemid, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("note", note);
			return "noteedit";
		} else {
			note.setCreatedUtc();
			note.setModifiedUtc();
			noteRepo.save(note);
			model.addAttribute("listItems", shoppingListRepo.findOne(id).getListItems());
			return "redirect:/list/" + id;
		}
	}

	@GetMapping("/lists/az")
	public String orderListsAz(Model model) {
		User currentUser = ListController.getCurrentUser();
		model.addAttribute("lists", shoppingListRepo.findAllByUserOrderByCategoryAsc(currentUser));
		return "lists";
	}

	@GetMapping("/lists/za")
	public String orderListsZa(Model model) {
		User currentUser = ListController.getCurrentUser();
		model.addAttribute("lists", shoppingListRepo.findAllByUserOrderByCategoryDesc(currentUser));
		return "lists";
	}

	@GetMapping("/lists/azname")
	public String orderListsAzname(Model model) {
		User currentUser = ListController.getCurrentUser();
		model.addAttribute("lists", shoppingListRepo.findAllByUserOrderByNameAsc(currentUser));
		return "lists";
	}

	@GetMapping("/lists/zaname")
	public String orderListsZaname(Model model) {
		User currentUser = ListController.getCurrentUser();
		model.addAttribute("lists", shoppingListRepo.findAllByUserOrderByNameDesc(currentUser));
		return "lists";
	}

	public static User getCurrentUser() {
		User currentUser = new User();
		String email = currentUser.getCurrentEmail();
		currentUser = userRepo.findOneByEmail(email);
		return currentUser;
	}

}
