package swp.happyprogramming.services;

import swp.happyprogramming.dto.PostDTO;

public interface IPostService {
    PostDTO findPost(long id);

    void updatePost(PostDTO postDTO,long postId, long method);
}
