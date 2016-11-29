package shoppinglistjava1.java.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import shoppinglistjava1.java.beans.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

}
