package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.socialmedia.model.File;

public interface FileRepository extends ListCrudRepository<File, Long> {
}
