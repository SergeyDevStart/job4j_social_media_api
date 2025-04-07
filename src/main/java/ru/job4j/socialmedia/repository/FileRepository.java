package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmedia.model.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByPostId(Long id);
}
