package com.example.easynotes.repository;

import com.example.easynotes.model.Note;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {


}
