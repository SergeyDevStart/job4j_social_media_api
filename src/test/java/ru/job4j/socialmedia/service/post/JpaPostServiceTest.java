package ru.job4j.socialmedia.service.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.model.File;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.FileRepository;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JpaPostServiceTest {
    @Autowired
    private JpaPostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${file.directory}")
    private String storageDirectory;

    @AfterEach
    void cleanUp() throws IOException {
        Path storage = Path.of(storageDirectory);
        if (Files.exists(storage)) {
            try (Stream<Path> files = Files.list(storage)) {
                files.map(Path::toFile).forEach(java.io.File::delete);
            }
        }
    }

    @Test
    void whenCreatePostThenReturnOptionalPost() {
        Post post = new Post();
        post.setTitle("Title");
        post.setContent("Content");
        MockMultipartFile file1 = new MockMultipartFile("testFile1", "testFile1.txt", "text/plain",  "Test content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("testFile2", "testFile2.txt", "text/plain",  "Test content".getBytes());

        Optional<Post> optionalPost = postService.create(post, new MultipartFile[]{file1, file2});

        assertThat(optionalPost).isPresent();
        Post savedPost = optionalPost.get();
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Title");
        assertThat(savedPost.getContent()).isEqualTo("Content");

        List<File> savedFiles = fileRepository.findByPostId(savedPost.getId());
        assertThat(savedFiles).hasSize(2);
        assertThat(savedFiles).extracting(File::getPost).contains(savedPost);
        assertThat(savedFiles).extracting(File::getName).containsExactlyInAnyOrder("testFile1.txt", "testFile2.txt");
    }

    @Test
    void whenDeletePostThenNotFound() {
        Post post = new Post();
        post.setTitle("Title");
        post.setContent("Content");
        MockMultipartFile file1 = new MockMultipartFile("testFile1", "testFile1.txt", "text/plain",  "Test content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("testFile2", "testFile2.txt", "text/plain",  "Test content".getBytes());
        postService.create(post, new MultipartFile[]{file1, file2});

        boolean isDeleted = postService.delete(post);

        assertThat(isDeleted).isTrue();
        assertThat(postRepository.findById(post.getId())).isEmpty();
        assertThat(fileRepository.findByPostId(post.getId())).hasSize(0);
    }

    @Test
    void whenUpdatePostWithFile() {
        Post post = new Post();
        post.setTitle("Title");
        post.setContent("Content");
        MockMultipartFile file1 = new MockMultipartFile("testFile1", "testFile1.txt", "text/plain",  "Test content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("testFile2", "testFile2.txt", "text/plain",  "Test content".getBytes());
        postService.create(post, new MultipartFile[]{file1, file2});

        post.setTitle("Updated Title");
        post.setContent("Updated Content");
        MockMultipartFile updatedFile = new MockMultipartFile("update", "update.txt", "text/plain",  "update".getBytes());

        boolean isUpdated = postService.update(post, new MultipartFile[]{updatedFile});
        Optional<Post> updatedPost = postRepository.findById(post.getId());

        assertThat(isUpdated).isTrue();
        assertThat(updatedPost).isPresent();
        assertThat(updatedPost.get().getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.get().getContent()).isEqualTo("Updated Content");

        List<File> savedFiles = fileRepository.findByPostId(updatedPost.get().getId());
        assertThat(savedFiles).hasSize(1);
        assertThat(savedFiles).extracting(File::getPost).contains(updatedPost.get());
        assertThat(savedFiles).extracting(File::getName).contains("update.txt");
    }

    @Test
    void whenGetUsersWithPostsByUserIdsThenGetUserDtoList() {
        User userOne = new User();
        userOne.setName("nameOne");
        userOne.setEmail("emailOne");
        userOne.setPassword("pass");
        User userTwo = new User();
        userTwo.setName("userTwo");
        userTwo.setEmail("emailTwo");
        userTwo.setPassword("pass");
        userRepository.save(userOne);
        userRepository.save(userTwo);
        Post postOne = new Post();
        postOne.setTitle("T1");
        postOne.setContent("C1");
        postOne.setUser(userOne);
        Post postTwo = new Post();
        postTwo.setTitle("T2");
        postTwo.setContent("C2");
        postTwo.setUser(userTwo);
        postRepository.save(postOne);
        postRepository.save(postTwo);

        List<Long> userIds = List.of(userOne.getId(), userTwo.getId());

        List<UserDto> result = postService.getUsersWithPostsByUserIds(userIds);

        assertThat(result).hasSize(2);
    }
}