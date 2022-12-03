package com.tyc.mapper;

import com.tyc.dto.request.CreatePostDto;
import com.tyc.dto.response.GetAllPost;
import com.tyc.repository.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IPostMapper {


    IPostMapper INSTANCE = Mappers.getMapper(IPostMapper.class);


    Post toPost(final CreatePostDto dto);

    List<GetAllPost> toGetAllPosts(final List<Post> posts);

    GetAllPost toGetAllPost(final Post post);
}
