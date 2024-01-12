package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFound;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepo;

    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id:" + postId)
        );
        Comment comment= mapToEntity(commentDto);
        comment.setPost(post);
        Comment c = commentRepo.save(comment);
        return  mapToDto(c);

    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id:" + postId)
        );
        commentRepo.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment>comments=commentRepo.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {
        Comment com = commentRepo.findById(commentId).get();
        Post post= postRepo.findById(com.getPost().getId()).get();
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        comment.setId(commentId);

        Comment savedComment = commentRepo.save(comment);
        CommentDto dto = mapToDto(savedComment);
        return dto;
    }


    CommentDto mapToDto(Comment comment) {
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
//        CommentDto dto= new CommentDto();
//        dto.setName(comment.getName());
//        dto.setBody(comment.getBody());
//        dto.setEmail(comment.getEmail());
        return dto;

    }

     Comment mapToEntity(CommentDto dto) {
         Comment comment = modelMapper.map(dto, Comment.class);
//        Comment comment= new Comment();
//        comment.setName(dto.getName());
//        comment.setBody(dto.getBody());
//        comment.setEmail(dto.getEmail());
        return comment;

    }


}
