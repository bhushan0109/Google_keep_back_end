package com.finovate.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finovate.exception.AuthorizationException;
import com.finovate.exception.NoteException;
import com.finovate.exception.ReminderException;
import com.finovate.model.Note;
import com.finovate.model.NoteDto;
import com.finovate.model.ReminderDto;
import com.finovate.model.User;
import com.finovate.repository.INoteRepository;
import com.finovate.repository.IUserRepository;
import com.finovate.util.JwtGenerator;


@Service
public class Noteservice implements INoteService{
	
	
	@Autowired
	private IUserRepository urepo;
	@Autowired
	private INoteRepository nrepo;
	@Autowired
	private JwtGenerator tokenobj;
	
	private User authenticatedUser(String token) {
		User fetchedUser = urepo.getUser(tokenobj.decodeToken(token));
		if (fetchedUser != null) {
			return fetchedUser;
		}
		throw new AuthorizationException("Authorization failed", 401);
	}
	
	@Override
	public boolean createNote(NoteDto noteDto, String token) {
		// found authorized user
		User fetchedUser = authenticatedUser(token);
		Note newNote = new Note();
		BeanUtils.copyProperties(noteDto, newNote);
		newNote.setCreatedDate(LocalDateTime.now());
		newNote.setColor("white");
		fetchedUser.getNotes().add(newNote);
		nrepo.saveOrUpdate(newNote);
		return true;
	}
	private Note verifiedNote(long noteId) {
		Note fetchedNote = nrepo.getNote(noteId);
		if (fetchedNote != null) {
			return fetchedNote;
		}
		throw new NoteException("Not not found", 300);
	}
	
	@Override
	public boolean deleteNote(long noteId, String token) {
		// found authorized user
		authenticatedUser(token);
		// verified valid note
		verifiedNote(noteId);
		nrepo.isDeletedNote(noteId);
		return true;
	}
	
	@Override
	public boolean updateNote(NoteDto noteDto, long noteId, String token) {
		// found authorized user
		authenticatedUser(token);
		// verified valid note
		Note fetchedNote = verifiedNote(noteId);
		BeanUtils.copyProperties(noteDto, fetchedNote);
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		nrepo.saveOrUpdate(fetchedNote);
		return true;
	}
	
	@Override
	public List<Note> getallNotes(String token) {
		// found authorized user
		User fetchedUser = authenticatedUser(token);
		// note found
		List<Note> fetchedNotes = nrepo.getAllNotes(fetchedUser.getId());
		if (!fetchedNotes.isEmpty()) {
			return fetchedNotes;
		}
		// empty list
		return fetchedNotes;
	}
	
	@Override
	public boolean archiveNote(long noteId, String token) {
		// found authorized user
		authenticatedUser(token);
		// verified valid note
		Note fetchedNote = verifiedNote(noteId);
		// fetched note is not archived
		if (!fetchedNote.isArchived()) {
			fetchedNote.setArchived(true);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			nrepo.saveOrUpdate(fetchedNote);
			return true;
		}
		// if archived already
		return false;
	}
	
	@Override
	public boolean pinNote(long noteId, String token) {
		// found authorized user
		authenticatedUser(token);
		// verified valid note
		Note fetchedNote = verifiedNote(noteId);
		if (!fetchedNote.isPinned()) {
			fetchedNote.setPinned(true);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			nrepo.saveOrUpdate(fetchedNote);
			return true;
		}
		// if pinned already
		fetchedNote.setPinned(false);
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		nrepo.saveOrUpdate(fetchedNote);
		return false;
	}
	
	@Override
	public void changeColour(String token, long noteId, String noteColour) {
		// authenticate user
		authenticatedUser(token);
		// validate note
		Note fetchedNote = verifiedNote(noteId);
		fetchedNote.setColor(noteColour);
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		nrepo.saveOrUpdate(fetchedNote);
	}
	
	@Override
	public boolean trashNote(long noteId, String token) {
		// found authorized user
		authenticatedUser(token);
		// verified valid note
		Note fetchedNote = verifiedNote(noteId);
		if (!fetchedNote.isTrashed()) {
			fetchedNote.setTrashed(true);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			nrepo.saveOrUpdate(fetchedNote);
			return true;
		}
		// if trashed already
		return false;
	}
	
	@Override
	public void addReminder(String token, long noteId, ReminderDto remainderDTO) {
		// authenticate user
		authenticatedUser(token);
		// validate note
		Note fetchedNote = verifiedNote(noteId);
		if (fetchedNote.getRemainderDate() == null) {
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedNote.setRemainderDate(remainderDTO.getRemainder());
			nrepo.saveOrUpdate(fetchedNote);
			return;
		}
		throw new ReminderException("Reminder already set!", 502);
	}
	@Override
	public void deleteReminder(String token, long noteId) {
		// authenticate user
		authenticatedUser(token);
		// validate note
		Note fetchedNote = verifiedNote(noteId);
		if (fetchedNote.getRemainderDate() != null) {
			fetchedNote.setRemainderDate(null);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			nrepo.saveOrUpdate(fetchedNote);
			return;
		}
		throw new ReminderException("Reminder already removed!", 502);
	}
	
	@Transactional
	@Override
	public boolean restored(String token, Long noteId) {
		
		authenticatedUser(token);
		
		Note fetchedNote=verifiedNote(noteId);
		if(fetchedNote.isTrashed()) {
			fetchedNote.setTrashed(false);
			fetchedNote.setCreatedDate(LocalDateTime.now());
			nrepo.saveOrUpdate(fetchedNote);
			return true;
			
		}
		return false;
	
	}
	@Override
	public List<Note> getTrashed(String token) {
		// note found of authenticated user
		List<Note> fetchedTrashedNotes = nrepo.getTrashed(authenticatedUser(token).getId());
		if (!fetchedTrashedNotes.isEmpty()) {
			return fetchedTrashedNotes;
		}
		// empty list
		return fetchedTrashedNotes;
	}
	
	@Override
	public List<Note> getPinned(String token) {
		
		List<Note> fetchedPinnedNotes = nrepo.getPinned(authenticatedUser(token).getId());
		if (!fetchedPinnedNotes.isEmpty()) {
			return fetchedPinnedNotes;
		}
		
		return fetchedPinnedNotes;
	}

}
