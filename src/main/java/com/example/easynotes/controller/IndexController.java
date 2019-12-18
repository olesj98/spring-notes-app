package com.example.easynotes.controller;


import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    NoteRepository noteRepository;

    @PersistenceContext
    EntityManager em;

    public Note getNoteByIdWithCriteria(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> noteRoot = query.from(Note.class);
        query.select(noteRoot).where(cb.equal(noteRoot.get("id"), id));

        TypedQuery<Note> qry = em.createQuery(query);

        Note note = qry.getSingleResult();
        return  note;
    }

    public List<Note> getNotesWithCriteria() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> noteRoot = query.from(Note.class);
        query.select(noteRoot);

        TypedQuery<Note> qry = em.createQuery(query);

        List<Note> notes = qry.getResultList();
        return  notes;
    }

    @GetMapping("/greeting")
    public String greeting(Model model) {
        List<Note> notes = getNotesWithCriteria();

        model.addAttribute("notes", notes);
        return "greeting";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(value = "id") Long noteId, Model model) {
        Note note = getNoteByIdWithCriteria(noteId);

        model.addAttribute("note", note);
        return "edit";
    }

}
