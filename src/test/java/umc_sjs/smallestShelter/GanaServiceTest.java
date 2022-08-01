package umc_sjs.smallestShelter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import umc_sjs.smallestShelter.domain.Post;
import umc_sjs.smallestShelter.service.PostService;
import umc_sjs.smallestShelter.domain.Animal;
import umc_sjs.smallestShelter.dto.post.CreatePostReq;
import umc_sjs.smallestShelter.dto.post.CreatePostRes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class GanaServiceTest {

    @Autowired private PostService postService;
    @PersistenceContext
    private EntityManager em;

    @Test
    public void 게시물등록(){
        // given
        Long animalIdx = 1L;
        CreatePostReq createPostReq = new CreatePostReq("/img", "내용내용");

        try{
            CreatePostRes createPostRes = postService.create(animalIdx, createPostReq);
            System.out.println("createPostRes.getPostIdx() = " + createPostRes.getPostIdx());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void 동물찾기(){
        Animal animal = em.find(Animal.class, 1L);
        System.out.println("animal.getName() = " + animal.getName());
    }

    @Test
    public void 동물찾기_없는동물조회(){
        Animal animal = em.find(Animal.class, 1110L);

        Assertions.assertThat(animal).isNull();
    }
    
    @Test
    public void 동물수정후_테이블확인(){
        Animal animal = em.find(Animal.class, 2L);

        System.out.println("animal.getPostList() = " + animal.getPostList());
    }

    @Test
    public void 게시물조회(){
        Post post = em.find(Post.class, 4L);
    }
}
