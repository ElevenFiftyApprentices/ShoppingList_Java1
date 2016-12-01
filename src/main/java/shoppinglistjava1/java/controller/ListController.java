package shoppinglistjava1.java.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ListItemRepository listItemRepo;

	@Autowired
	private ShoppingListRepository shoppingListRepo;

	@Autowired
	private NoteRepository noteRepo;

	@GetMapping("/list/{id}")
	public String list(Model model, @PathVariable(name = "id") long id, @RequestParam(name = "srch-term", required = false) String searchTerm) {
		model.addAttribute("id", id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		ShoppingList l = shoppingListRepo.findOne(id);
		if (v.getShoppingLists().contains(l)) {
			model.addAttribute("shoppingList", l);
		}
		if (searchTerm == null || "".equals(searchTerm)) {
			model.addAttribute("listItems", l.getListItems());
		} else {
			List<ListItem> li = listItemRepo.findByContentsContainsOrNoteContainsAllIgnoreCase(searchTerm, searchTerm);
			ArrayList<ListItem> li2 = new ArrayList();
			for(ListItem lix : li){
				if(l.getListItems().contains(lix)){
					li2.add(lix);
				}
			}
			model.addAttribute("listItems", li2);
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
		
		Note n = new Note();
		ListItem li = new ListItem();
		li.setNote(n);
		model.addAttribute("note", n);
		model.addAttribute("item", li);

		return "listitemadd";
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
			item.setCreatedUtc(new Date(System.currentTimeMillis()));
			item.setModifiedUtc(new Date(System.currentTimeMillis()));
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
	public String listItemEdit(Model model, @PathVariable(name = "id") long id, @PathVariable(name = "itemid") long itemid) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		model.addAttribute("list", shoppingListRepo.findOne(id));
		model.addAttribute("item", listItemRepo.findOne(itemid));

		return "listitemedit";
	}

	@PostMapping("/list/{id}/edititem/{itemid}")
	public String listItemEditSave(Model model, @PathVariable(name = "id") long id, @ModelAttribute @Valid ListItem item, @PathVariable(name = "itemid") long itemid,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("item", item);
			return "listitemedit";
		} else {
			item.setList(shoppingListRepo.findOne(id));
			item.setCreatedUtc(new Date(System.currentTimeMillis()));
			item.setModifiedUtc(new Date(System.currentTimeMillis()));
			listItemRepo.save(item);
			model.addAttribute("listItems", shoppingListRepo.findOne(id).getListItems());
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
	public String listAddSave(Model model, @ModelAttribute @Valid ShoppingList list, BindingResult result) {
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

	@GetMapping("/list/{id}/delete/{itemid}")
	public String listItemDelete(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		model.addAttribute("list", shoppingListRepo.findOne(id));
		model.addAttribute("item", listItemRepo.findOne(itemid));
		return "listitemdelete";
	}

	@PostMapping("/list/{id}/delete/{itemid}")
	public String listItemDeleteSave(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
			ShoppingList l = shoppingListRepo.findOne(id);
			List<ListItem> li = l.getListItems();
			listItemRepo.delete(listItemRepo.findOne(itemid));
			li.remove(listItemRepo.findOne(itemid));
			model.addAttribute("list", l);
			return "redirect:/list/" + id;
	}

	@GetMapping("/list/{id}/delete")
	public String listDelete(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		model.addAttribute("list", shoppingListRepo.findOne(id));
		return "deletelist";
	}

	@PostMapping("/list/{id}/delete")
	public String listDeleteSave(Model model, @PathVariable(name = "id") long id,
			@ModelAttribute @Valid ShoppingList list, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("list", list);
			return "deletelist";
		} else {
			shoppingListRepo.delete(list);
			model.addAttribute("listItems", list.getListItems());
			return "redirect:/lists";
		}
	}

	@GetMapping("/list/{id}/check/{itemid}")
	public String listItemCheck(Model model, @PathVariable(name = "id") long id,
			@PathVariable(name = "itemid") long itemid) {
		ListItem i = listItemRepo.findOne(itemid);
		i.setChecked(true);
		listItemRepo.save(i);
		model.addAttribute("shoppingList", shoppingListRepo.findOne(id));
		model.addAttribute("listItems",  shoppingListRepo.findOne(id).getListItems());
		return "listview";
	}

	@GetMapping("/list/{id}/clearchecked")
	public String clearChecked(Model model, @PathVariable(name = "id") long id) {
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

	@GetMapping("/list/{id}/deletechecked")
	public String deleteChecked(Model model, @PathVariable(name = "id") long id) {
		ShoppingList l = shoppingListRepo.findOne(id);
		model.addAttribute("list", l);
		model.addAttribute("listItems", l.getListItems());
		return "deletechecked";
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
			for(ListItem i2 : liTwo){
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
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByPriorityAsc(l);
		for(ListItem lix : li){
		}		
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
	}
	
	@GetMapping("/list/{id}/low")
	public String orderLow(Model model, @PathVariable(name = "id") long id) {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByPriorityDesc(l);
		for(ListItem lix : li){
		}		
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
	}
	
	@GetMapping("/list/{id}/az")
	public String orderAz(Model model, @PathVariable(name = "id") long id) {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByContentsAsc(l);
		for(ListItem lix : li){
		}		
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
	}
	
	@GetMapping("/list/{id}/za")
	public String orderZa(Model model, @PathVariable(name = "id") long id) {
		ShoppingList l = shoppingListRepo.findOne(id);
		List<ListItem> li = listItemRepo.findByListOrderByContentsDesc(l);
		for(ListItem lix : li){
		}		
		model.addAttribute("listItems", li);
		model.addAttribute("shoppingList", l);
		return "listview";
	}
	
	@GetMapping("/list/{id}/edititem/{itemid}/editnote")
	public String NoteEdit(Model model, @PathVariable(name = "id") long id, @PathVariable(name = "itemid") long itemid) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
		ShoppingList l = shoppingListRepo.findOne(id);
		ListItem li = listItemRepo.findOne(itemid);
		if(listItemRepo.findOne(itemid).getNote() == null){
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

	@PostMapping("/list/{id}/edititem/{itemid}/editnote")
	public String NoteEditSave(Model model, @PathVariable(name = "id") long id, @ModelAttribute @Valid Note note, @PathVariable(name = "itemid") long itemid,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("note", note);
			return "noteedit";
		} else {
			note.setCreatedUtc(new Date(System.currentTimeMillis()));
			note.setModifiedUtc(new Date(System.currentTimeMillis()));
			noteRepo.save(note);
			model.addAttribute("listItems", shoppingListRepo.findOne(id).getListItems());
			return "redirect:/list/" + id;
		}
	}

}
