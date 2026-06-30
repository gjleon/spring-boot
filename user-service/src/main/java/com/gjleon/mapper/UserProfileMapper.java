package com.gjleon.mapper;

import com.gjleon.domain.User;
import com.gjleon.domain.UserProfile;
import com.gjleon.response.UserProfileGetResponse;
import com.gjleon.response.UserProfileUserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);


    List<UserProfileUserGetResponse> toUserProfileUserGetResponse(List<User> users);
}
