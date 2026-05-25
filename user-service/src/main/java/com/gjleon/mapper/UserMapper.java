package com.gjleon.mapper;

import com.gjleon.domain.User;
import com.gjleon.request.UserPostRequest;
import com.gjleon.request.UserPutRequest;
import com.gjleon.response.UserGetResponse;
import com.gjleon.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

    UserGetResponse toUserGetResponseList(User userFound);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    User toUser(UserPostRequest request);

    UserPostResponse toUserPostResponse(User userSave);

    User toUser(UserPutRequest request);
}
