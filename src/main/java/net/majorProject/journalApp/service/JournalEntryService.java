package net.majorProject.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.majorProject.journalApp.entity.JournalEntry;
import net.majorProject.journalApp.entity.User;
import net.majorProject.journalApp.entity.Weather;
import net.majorProject.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JournalEntryService {

@Autowired
private WeatherService weatherService;

@Autowired
public JournalEntryRepository journalEntryRepository;
@Autowired
public UserService userService;

@Transactional
public void saveEntry(JournalEntry journalEntry, String userName){

    User user =userService.findByUserName(userName);
    journalEntry.setDate(LocalDateTime.now());
    Weather weather = weatherService.getWeather("Bhopal");
    journalEntry.setWeather(weather);

    JournalEntry saved = journalEntryRepository.save(journalEntry);
    user.getJournalEntries().add(saved);
    userService.saveUser(user);
}

    public void saveEntry(JournalEntry journalEntry){
       journalEntryRepository.save(journalEntry);
    }
public List<JournalEntry> getAll(){
    return journalEntryRepository.findAll();
}

public Optional<JournalEntry> journalEntryById(ObjectId id){
    return journalEntryRepository.findById(id);
}

public JournalEntry findEntryByIdForSpeech(ObjectId id){
        return journalEntryRepository
                .findById(id)
                .orElse(null);
    }

@Transactional
public boolean deleteById(ObjectId id, String userName){
    boolean removed=false;
    try {
        User user=userService.findByUserName(userName);
        removed=user.getJournalEntries().removeIf( x -> x.getId().equals(id));
        if(removed){
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
    }
    catch (Exception e){
        log.error("deleting username {}",userName,e );
        throw new RuntimeException("an error occured during deleting the entry",e);
    }
    return removed;
}



}
