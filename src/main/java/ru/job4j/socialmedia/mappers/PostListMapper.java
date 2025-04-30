package ru.job4j.socialmedia.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.job4j.socialmedia.dto.PostDto;
import ru.job4j.socialmedia.model.Post;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PostMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostListMapper {
    List<Post> toPosts(List<PostDto> postsDto);

    List<PostDto> toPostsDto(List<Post> posts);
}
