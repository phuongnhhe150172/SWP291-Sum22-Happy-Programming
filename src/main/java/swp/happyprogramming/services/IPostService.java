package swp.happyprogramming.services;

import swp.happyprogramming.dto.PostDTO;

public interface IPostService {
    PostDTO findPost(long id);
}
