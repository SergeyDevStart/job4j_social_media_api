package ru.job4j.socialmedia.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.socialmedia.dto.PostDto;
import ru.job4j.socialmedia.dto.UserDto;
import ru.job4j.socialmedia.dto.UserWithPostsDto;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PostListMapper.class})
public interface UserMapper {
    @Mapping(target = "posts", source = "userPosts")
    UserWithPostsDto getUserDtoWithPosts(User user, List<Post> userPosts);

    User toUserFromUserDto(UserDto userDto);
}
