package ru.job4j.socialmedia.service.file;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.model.File;
import ru.job4j.socialmedia.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FileService {
    Set<File> fileProcessing(Post post, MultipartFile[] files);

    Optional<File> save(File file);

    void delete(File file);

    List<File> getAllFilesByPostId(Long id);

    void deleteAll(List<File> files);

    void flush();
}
