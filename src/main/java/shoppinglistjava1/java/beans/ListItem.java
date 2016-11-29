package shoppinglistjava1.java.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "listItems")
public class ListItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList list;
	
	private String contents;

	private int priority;
	
	private boolean isChecked;
	
	private Date createdUtc;
	
	private Date modifiedUtc;
	
	private long shoppingListId;
	
	@OneToMany(mappedBy="listItem")
	private List <Note> Notes;

	

	public ShoppingList getList() {
		return list;
	}

	public void setList(ShoppingList list) {
		this.list = list;
	}

	public List<Note> getNotes() {
		return Notes;
	}

	public void setNotes(List<Note> notes) {
		Notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Date getCreatedUtc() {
		return createdUtc;
	}

	public void setCreatedUtc(Date createdUtc) {
		this.createdUtc = createdUtc;
	}

	public Date getModifiedUtc() {
		return modifiedUtc;
	}

	public void setModifiedUtc(Date modifiedUtc) {
		this.modifiedUtc = modifiedUtc;
	}

	public long getShoppingListId() {
		return shoppingListId;
	}

	public void setShoppingListId(long shoppingListId) {
		this.shoppingListId = shoppingListId;
	}
	
	public String getPriorityText(int priority){
		String prioText = "";
		for(PriorityTexts text : PriorityTexts.values()){
			if(text.getNum() == priority){
				prioText = text.getText();
			}
		}
		return prioText;
	}
	
}
