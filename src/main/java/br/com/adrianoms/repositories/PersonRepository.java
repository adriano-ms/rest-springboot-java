package br.com.adrianoms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.adrianoms.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
}
