package shoppinglistjava1.java.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notes")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne 
    @JoinColumn(name="shopping_list_item_id") 
    private ListItem listItem;

	
	private String body;
	
	private Date createdUtc;
	
	private Date modifiedUtc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	

	public ListItem getListItem() {
		return listItem;
	}

	public void setListItem(ListItem listItem) {
		this.listItem = listItem;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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
	
	
	
	
	
	
}
