package net.majorProject.journalApp.controller;

import net.majorProject.journalApp.entity.JournalEntry;
import net.majorProject.journalApp.entity.User;
import net.majorProject.journalApp.service.JournalEntryService;
import net.majorProject.journalApp.service.TextToSpeechService;
import net.majorProject.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController_v2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @Autowired
    private TextToSpeechService textToSpeechService;


    @GetMapping
    public ResponseEntity<?> getAll(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user =userService.findByUserName(userName);
        List<JournalEntry> all=user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try{
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String userName=authentication.getName();
            journalEntryService.saveEntry(myEntry,userName);//localhost:8080/journal post
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId ){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry= journalEntryService.journalEntryById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        boolean removed = journalEntryService.deleteById(myId,userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateEntrybyId(@PathVariable ObjectId myId , @RequestBody JournalEntry myEntry)
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();

        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.journalEntryById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry oldEntry=journalEntry.get();
                oldEntry.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().equals("") ? myEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(myEntry.getContent() != null && !myEntry.getContent().equals("") ? myEntry.getContent() : oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}/listen")
    public ResponseEntity<byte[]> listenJournal(@PathVariable ObjectId id) {
        JournalEntry entry = journalEntryService.findEntryByIdForSpeech(id);

        if(entry == null) {
            return ResponseEntity.notFound().build();
        }
        String text =
                entry.getTitle()
                        + ". "
                        + entry.getContent();
        byte[] audio =
                textToSpeechService
                        .convertTextToSpeech(text);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.valueOf("audio/mpeg"));

        return new ResponseEntity<>(
                audio,
                headers,
                HttpStatus.OK
        );
    }
}
