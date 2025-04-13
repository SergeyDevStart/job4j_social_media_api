package ru.job4j.socialmedia.service.post;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.model.File;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.service.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class JpaPostService implements PostService {
    private final PostRepository postRepository;
    private final FileService fileService;

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean update(Post post) {
        return postRepository.updateTitleAndContent(post.getTitle(), post.getContent(), post.getId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return postRepository.deletePostById(id) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Post> create(Post post, MultipartFile[] multipartFiles) {
        Post savedPost = postRepository.save(post);
        var files = fileService.fileProcessing(savedPost, multipartFiles);
        files.forEach(fileService::save);
        return Optional.of(savedPost);
    }

    @Override
    @Transactional
    public boolean update(Post post, MultipartFile[] multipartFiles) {
        if (multipartFiles.length != 0 && multipartFiles[0].getSize() != 0) {
            fileService.deleteAll(fileService.getAllFilesByPostId(post.getId()));
            fileService.flush();
            var filesToSave = fileService.fileProcessing(post, multipartFiles);
            filesToSave.forEach(fileService::save);
        }
        return postRepository.updateTitleAndContent(post.getTitle(), post.getContent(), post.getId()) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Post post) {
        List<File> filesToDelete = fileService.getAllFilesByPostId(post.getId());
        if (!filesToDelete.isEmpty()) {
            fileService.deleteAll(filesToDelete);
        }
        return postRepository.deletePostById(post.getId()) > 0;
    }
}
