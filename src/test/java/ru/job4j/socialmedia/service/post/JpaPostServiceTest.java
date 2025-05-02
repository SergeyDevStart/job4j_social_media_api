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
import ru.job4j.socialmedia.dto.PostDto;
import ru.job4j.socialmedia.model.File;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.repository.FileRepository;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
        PostDto postDto = new PostDto();
        postDto.setTitle("Title");
        postDto.setContent("Content");
        MockMultipartFile file1 = new MockMultipartFile("testFile1", "testFile1.txt", "text/plain",  "Test content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("testFile2", "testFile2.txt", "text/plain",  "Test content".getBytes());

        Post post = postService.create(postDto, new MultipartFile[]{file1, file2});

        assertThat(post.getId()).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Title");
        assertThat(post.getContent()).isEqualTo("Content");

        List<File> savedFiles = fileRepository.findByPostId(post.getId());
        assertThat(savedFiles).hasSize(2);
        assertThat(savedFiles).extracting(File::getPost).contains(post);
        assertThat(savedFiles).extracting(File::getName).containsExactlyInAnyOrder("testFile1.txt", "testFile2.txt");
    }

    @Test
    void whenDeletePostThenNotFound() {
        PostDto postDto = new PostDto();
        postDto.setTitle("Title");
        postDto.setContent("Content");
        MockMultipartFile file1 = new MockMultipartFile("testFile1", "testFile1.txt", "text/plain",  "Test content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("testFile2", "testFile2.txt", "text/plain",  "Test content".getBytes());

        Post post = postService.create(postDto, new MultipartFile[]{file1, file2});

        boolean isDeleted = postService.delete(post);

        assertThat(isDeleted).isTrue();
        assertThat(postRepository.findById(post.getId())).isEmpty();
        assertThat(fileRepository.findByPostId(post.getId())).hasSize(0);
    }

    @Test
    void whenUpdatePostWithFile() {
        PostDto postDto = new PostDto();
        postDto.setTitle("Title");
        postDto.setContent("Content");
        MockMultipartFile file1 = new MockMultipartFile("testFile1", "testFile1.txt", "text/plain",  "Test content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("testFile2", "testFile2.txt", "text/plain",  "Test content".getBytes());

        Post post = postService.create(postDto, new MultipartFile[]{file1, file2});

        PostDto postDtoForUpdate = new PostDto();
        postDtoForUpdate.setId(post.getId());
        postDtoForUpdate.setTitle("Updated Title");
        postDtoForUpdate.setContent("Updated Content");
        MockMultipartFile updatedFile = new MockMultipartFile("update", "update.txt", "text/plain",  "update".getBytes());

        boolean isUpdated = postService.update(postDtoForUpdate, new MultipartFile[]{updatedFile});
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
}