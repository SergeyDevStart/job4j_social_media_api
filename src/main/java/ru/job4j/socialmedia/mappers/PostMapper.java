package ru.job4j.socialmedia.mappers;

import org.mapstruct.Mapper;
import ru.job4j.socialmedia.dto.PostDto;
import ru.job4j.socialmedia.model.Post;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPostFromPostDto(PostDto postDto);
}
