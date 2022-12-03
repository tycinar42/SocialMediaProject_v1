package com.tyc.mapper;

import com.tyc.dto.request.CreateCommentDto;
import com.tyc.repository.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ICommentMapper {


    ICommentMapper INSTANCE = Mappers.getMapper(ICommentMapper.class);


    Comment toComment(final CreateCommentDto dto);
}
