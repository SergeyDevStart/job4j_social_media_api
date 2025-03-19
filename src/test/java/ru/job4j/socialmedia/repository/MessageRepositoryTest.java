package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Message;
import ru.job4j.socialmedia.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    private User sender;
    private User receiver;

    @BeforeEach
    public void setUp() {
        sender = new User();
        sender.setName("senderUser");
        sender.setEmail("sender@mail.ru");
        sender.setPassword("password");
        userRepository.save(sender);
        receiver = new User();

        receiver.setName("receiverUser");
        receiver.setEmail("receiver@mail.ru");
        receiver.setPassword("password");
        userRepository.save(receiver);
    }

    @Test
    void whenSaveThenFindById() {
        var message = new Message();
        message.setMessage("message");
        message.setSender(sender);
        message.setReceiver(receiver);
        messageRepository.save(message);

        var foundMessage = messageRepository.findById(message.getId());

        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get()).isEqualTo(message);
    }

    @Test
    void whenFindAllThenFindAllMessage() {
        var messageOne = new Message();
        messageOne.setMessage("messageOne");
        messageOne.setSender(sender);
        messageOne.setReceiver(receiver);
        messageRepository.save(messageOne);
        var messageTwo = new Message();
        messageTwo.setMessage("messageTwo");
        messageTwo.setSender(receiver);
        messageTwo.setReceiver(sender);
        messageRepository.save(messageTwo);

        var expected = List.of(messageOne, messageTwo);
        var result = messageRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenDeleteThenNotFound() {
        var message = new Message();
        message.setMessage("message");
        message.setSender(sender);
        message.setReceiver(receiver);
        messageRepository.save(message);

        messageRepository.delete(message);

        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }
}