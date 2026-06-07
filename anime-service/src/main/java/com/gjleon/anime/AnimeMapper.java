package com.gjleon.anime;

import com.gjleon.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest request);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> anime);

    AnimePostResponse toAnimePostResponse(Anime anime);

}

