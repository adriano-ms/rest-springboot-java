package br.com.adrianoms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.adrianoms.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
