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
	private long userId; 
	
	@Size(max = 100)
	private String name;

	@Size(max = 100)
	private String color;
	
	private Date createdUTC;
	
	private Date modifiedUTC;

	public ShoppingList(long id, long userId, String name, String color, Date createdUTC, Date modifiedUTC) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.color = color;
		this.createdUTC = createdUTC;
		this.modifiedUTC = modifiedUTC;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((createdUTC == null) ? 0 : createdUTC.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((modifiedUTC == null) ? 0 : modifiedUTC.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingList other = (ShoppingList) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (createdUTC == null) {
			if (other.createdUTC != null)
				return false;
		} else if (!createdUTC.equals(other.createdUTC))
			return false;
		if (id != other.id)
			return false;
		if (modifiedUTC == null) {
			if (other.modifiedUTC != null)
				return false;
		} else if (!modifiedUTC.equals(other.modifiedUTC))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
	
	
	
}
