package shoppinglistjava1.java.beans;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "listItems")
public class ListItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList list;
	
	private String contents;

	private Integer priority;
	
	public boolean isChecked;
	
	private Date createdUtc;
	
	private Date modifiedUtc;
	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "note_id")
	private Note note;


	

	public ShoppingList getList() {
		return list;
	}

	public void setList(ShoppingList list) {
		this.list = list;
	}



	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
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

	public void setCreatedUtc() {
		this.createdUtc = new Date(System.currentTimeMillis());
	}

	public Date getModifiedUtc() {
		return modifiedUtc;
	}

	public void setModifiedUtc() {
		this.modifiedUtc = new Date(System.currentTimeMillis());
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
