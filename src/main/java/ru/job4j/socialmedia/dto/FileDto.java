package ru.job4j.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.job4j.socialmedia.model.Post;

@AllArgsConstructor
@Setter
@Getter
public class FileDto {
    private String name;
    private byte[] content;
}
