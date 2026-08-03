package net.majorProject.journalApp.service;

import net.majorProject.journalApp.entity.User;
import net.majorProject.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUserNameTest(){
        assertNotNull(userRepository.findByUserName("aashish"));
    }


    @Disabled
    @Test
    public void journalPresenceTest(){
       User user=userRepository.findByUserName("aashish");
       assertTrue(!user.getJournalEntries().isEmpty());
    }


    @ParameterizedTest
    @CsvSource({"2,2,4",
                "3,3,6"})
    public void test(int a, int b, int expected){
        assertEquals(expected,a+b);
    }
}
