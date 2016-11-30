package shoppinglistjava1.java.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user; 
	
	@Size(max = 100)
	private String name;

	@Size(max = 100)
	private String color;
	
	private Date createdUtc;
	
	private Date modifiedUtc;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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
		return createdUtc;
	}

	public void setCreatedUTC(Date createdUTC) {
		this.createdUtc = createdUTC;
	}

	public Date getModifiedUTC() {
		return modifiedUtc;
	}

	public void setModifiedUTC(Date modifiedUTC) {
		this.modifiedUtc = modifiedUTC;
	}

	
	
	public List<ListItem> getListItems() {
		return listItems;
	}

	public void setListItems(List<ListItem> listItems) {
		this.listItems = listItems;
	}

	
	
	
	
}
