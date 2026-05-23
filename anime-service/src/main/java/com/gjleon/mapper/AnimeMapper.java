package com.gjleon.mapper;

import com.gjleon.domain.Anime;
import com.gjleon.request.AnimePostRequest;
import com.gjleon.request.AnimePutRequest;
import com.gjleon.resonse.AnimeGetResponse;
import com.gjleon.resonse.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest request);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> anime);

    AnimePostResponse toAnimePostResponse(Anime anime);

}

