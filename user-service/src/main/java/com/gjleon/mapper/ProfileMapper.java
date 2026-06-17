package com.gjleon.mapper;

import com.gjleon.domain.Profile;
import com.gjleon.request.ProfilePostRequest;
import com.gjleon.response.ProfileGetResponse;
import com.gjleon.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    Profile toProfile(ProfilePostRequest postRequest);

    ProfilePostResponse toProfilePostResponse(Profile profile);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profiles);
}
