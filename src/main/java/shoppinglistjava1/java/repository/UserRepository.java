package shoppinglistjava1.java.repository;

import org.springframework.data.repository.CrudRepository;

import shoppinglistjava1.java.beans.User;

public interface UserRepository extends CrudRepository<User, Long>{

}
