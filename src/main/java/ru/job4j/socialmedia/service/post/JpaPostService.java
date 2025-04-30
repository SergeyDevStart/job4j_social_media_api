package ru.job4j.socialmedia.service.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.dto.PostDto;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.mappers.PostMapper;
import ru.job4j.socialmedia.mappers.UserMapper;
import ru.job4j.socialmedia.model.File;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.UserRepository;
import ru.job4j.socialmedia.service.file.FileService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class JpaPostService implements PostService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PostRepository postRepository;
    private final FileService fileService;
    private final PostMapper postMapper;

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
    public Post create(PostDto postDto, MultipartFile[] multipartFiles) {
        Post post = postMapper.toPostFromPostDto(postDto);
        Post savedPost = postRepository.save(post);
        if (validFiles(multipartFiles)) {
            var files = fileService.fileProcessing(savedPost, multipartFiles);
            files.forEach(fileService::save);
        }
        return savedPost;
        }

        @Override
        @Transactional
        public boolean update(PostDto postDto, MultipartFile[] multipartFiles) {
        Post post = postMapper.toPostFromPostDto(postDto);
        if (validFiles(multipartFiles)) {
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

    @Override
    @Transactional(readOnly = true)
    public List<UserWithPostsDto> getUsersWithPostsByUserIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        List<Post> posts = postRepository.findByUserIdIn(ids);
        Map<Long, List<Post>> postsByUserId = posts.stream()
                .collect(Collectors.groupingBy(post -> post.getUser().getId()));
        return users.stream()
                .map(user -> {
                    List<Post> userPosts = postsByUserId.getOrDefault(user.getId(), List.of());
                    return mapper.getUserDtoWithPosts(user, userPosts);
                }).collect(Collectors.toList());
    }

    private boolean validFiles(MultipartFile[] multipartFiles) {
        return multipartFiles != null && multipartFiles.length != 0 && multipartFiles[0].getSize() != 0;
    }
}
