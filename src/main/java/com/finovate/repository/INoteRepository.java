package com.finovate.repository;

import java.util.List;

import com.finovate.model.Note;



public interface INoteRepository {

	public Note saveOrUpdate(Note newNote);

	public Note getNote(long noteId);

	public boolean isDeletedNote(long noteId);

	public List<Note> getAllNotes(long userId);
	public List<Note> getTrashed(long userId);
	public List<Note> getPinned(long userId);


}