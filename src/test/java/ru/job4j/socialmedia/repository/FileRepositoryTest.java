package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.File;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FileRepositoryTest {
    @Autowired
    private FileRepository fileRepository;

    @Test
    void whenSaveFileThenFindById() {
        var file = new File();
        file.setName("file.txt");
        file.setPath("directory/");
        fileRepository.save(file);

        var foundFile = fileRepository.findById(file.getId());

        assertThat(foundFile).isPresent();
        assertThat(foundFile.get()).isEqualTo(file);
    }
    
    @Test
    void whenFindAllThenFindAllFiles() {
        var fileOne = new File();
        fileOne.setName("fileOne.txt");
        fileOne.setPath("directory/");
        fileRepository.save(fileOne);
        var fileTwo = new File();
        fileTwo.setName("fileTwo.txt");
        fileTwo.setPath("directory/two/");
        fileRepository.save(fileTwo);

        var expected = List.of(fileOne, fileTwo);
        var result = fileRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenDeleteThenNotFound() {
        var file = new File();
        file.setName("file.txt");
        file.setPath("directory/");
        fileRepository.save(file);
        fileRepository.delete(file);

        assertThat(fileRepository.findById(file.getId())).isEmpty();
    }
}