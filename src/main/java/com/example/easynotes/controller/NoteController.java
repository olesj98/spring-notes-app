package com.example.easynotes.controller;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/api")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;


    @GetMapping("/notes")
    public List<Note> getAllNotes() {
//        return noteRepository.findAll();
        try{
            return getNotesWithCriteria();
        } catch (Exception exc) {
            throw new ResourceNotFoundException("Note", "id", "notes");
        }

    }

    @PostMapping("/notes")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteRepository.save(note);
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
//        return noteRepository.findById(noteId)
//                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
        try {
            return getNoteByIdWithCriteria(noteId);
        } catch (Exception exc) {
            throw new ResourceNotFoundException("Note", "id", noteId);
        }
    }

    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody Note noteDetails) {
        try {
            Note note = getNoteByIdWithCriteria(noteId);

            note.setTitle(noteDetails.getTitle());
            note.setContent(noteDetails.getContent());

            Note updatedNote = noteRepository.save(note);
            return updatedNote;
        } catch (Exception exc) {
            throw new ResourceNotFoundException("Note", "id", noteId);
        }

    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        try{
            Note note = getNoteByIdWithCriteria(noteId);

            noteRepository.delete(note);

            return ResponseEntity.ok().build();
        } catch (Exception exc) {
            throw new ResourceNotFoundException("Note", "id", noteId);
        }

    }

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
}
