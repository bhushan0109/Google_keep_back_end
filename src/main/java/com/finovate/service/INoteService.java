package com.finovate.service;

import java.util.List;

import com.finovate.model.Note;
import com.finovate.model.NoteDto;
import com.finovate.model.ReminderDto;



public interface INoteService {

	public boolean createNote(NoteDto noteDto, String token);

	public boolean deleteNote(long noteId, String token);

	public boolean updateNote(NoteDto noteDto, long noteId, String token);

	public List<Note> getallNotes(String token);

	public boolean archiveNote(long noteId, String token);

	public boolean pinNote(long noteId, String token);

	public void changeColour(String token, long noteId, String noteColor);

	public boolean trashNote(long noteId, String token);

	public void addReminder(String token, long noteId, ReminderDto remainderDTO);
	
	public void deleteReminder(String token, long noteId);
	
	//public boolean restored(long noteId, String token);
	
	public List<Note> getTrashed(String token);
	
	public List<Note> getPinned(String token);

}