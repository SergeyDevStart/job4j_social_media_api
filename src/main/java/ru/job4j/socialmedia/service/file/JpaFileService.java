package ru.job4j.socialmedia.service.file;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.socialmedia.dto.FileDto;
import ru.job4j.socialmedia.model.File;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JpaFileService implements FileService {

    private final FileRepository fileRepository;
    @Value("${file.directory}")
    private String fileDirectory;

    @Override
    public Set<File> fileProcessing(Post post, MultipartFile[] files) {
        var fileDtoSet = getFileDtoSet(files);
        return fileDtoSet.stream()
                .map(fileDto -> {
                    File file = toFileFromFileDto(fileDto);
                    file.setPost(post);
                    return file;
                }).collect(Collectors.toSet());
    }

    @Override
    public Optional<File> save(File file) {
        return Optional.of(fileRepository.save(file));
    }

    @Override
    public void delete(File file) {
        fileRepository.delete(file);
    }

    @Override
    public void deleteAll(List<File> files) {
        for (File file : files) {
            deleteByPath(file.getPath());
            delete(file);
        }
    }

    private void deleteByPath(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            log.error("Error deleting file: {}", path, e);
        }
    }

    @Override
    public List<File> getAllFilesByPostId(Long id) {
        return fileRepository.findByPostId(id);
    }

    private File toFileFromFileDto(FileDto fileDto) {
        var path = getNewFileName(fileDto.getName());
        writeFileBytes(path, fileDto.getContent());
        return new File(fileDto.getName(), path);
    }

    private String getNewFileName(String sourceName) {
        return fileDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            log.error("Error writing file: {}", path, e);
        }
    }

    private FileDto getNewFileDto(String name, byte[] content) {
        return new FileDto(name, content);
    }

    private Set<FileDto> getFileDtoSet(MultipartFile[] files) {
        Set<FileDto> fileDtoSet = new HashSet<>();
        for (MultipartFile file : files) {
            try {
                fileDtoSet.add(getNewFileDto(file.getOriginalFilename(), file.getBytes()));
            } catch (IOException e) {
                log.warn("Failed to process file: {}", file.getOriginalFilename(), e);
            }
        }
        return fileDtoSet;
    }

    public void flush() {
        fileRepository.flush();
    }
}
