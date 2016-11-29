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
import javax.validation.constraints.Size;


@Entity
@Table(name = "lists")
public class ShoppingList {
	
	@OneToMany(mappedBy="list")
	private List <ListItem> listItems;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private long user; 
	
	@Size(max = 100)
	private String name;

	@Size(max = 100)
	private String color;
	
	private Date createdUTC;
	
	private Date modifiedUTC;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getCreatedUTC() {
		return createdUTC;
	}

	public void setCreatedUTC(Date createdUTC) {
		this.createdUTC = createdUTC;
	}

	public Date getModifiedUTC() {
		return modifiedUTC;
	}

	public void setModifiedUTC(Date modifiedUTC) {
		this.modifiedUTC = modifiedUTC;
	}

	
	
	public List<ListItem> getListItems() {
		return listItems;
	}

	public void setListItems(List<ListItem> listItems) {
		this.listItems = listItems;
	}

	
	
	
	
}
