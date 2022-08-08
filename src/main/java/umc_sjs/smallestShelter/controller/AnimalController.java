package umc_sjs.smallestShelter.controller;

import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc_sjs.smallestShelter.domain.*;
import umc_sjs.smallestShelter.dto.animal.*;
import umc_sjs.smallestShelter.dto.animal.getAnimalDetailDto.GetAnimalDetailRes;
import umc_sjs.smallestShelter.dto.animal.getAnimalDetailDto.IllnessDto;
import umc_sjs.smallestShelter.dto.animal.getAnimalDetailDto.PostDto;
import umc_sjs.smallestShelter.dto.animal.getAnimalDetailDto.RecommandAnimalDto;
import umc_sjs.smallestShelter.dto.animal.getAnimalDto.GetAnimalRes;
import umc_sjs.smallestShelter.response.BaseException;
import umc_sjs.smallestShelter.response.BaseResponse;
import umc_sjs.smallestShelter.response.BaseResponseStatus;
import umc_sjs.smallestShelter.service.*;
import umc_sjs.smallestShelter.repository.*;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@NoArgsConstructor
@Validated
public class AnimalController {

    private final AnimalService animalService;
    private final AnimalRepository animalRepository;
    private final PostService postService;

    /**
     * 동물 등록 API
     * @param joinAnimalReq
     * @return JoinAnimalRes
     */
    //@ResponseBody
    @PostMapping("/auth/organization/animal/join")
    public BaseResponse<JoinAnimalRes> joinAnimal(@RequestBody JoinAnimalReq joinAnimalReq) {

        if (joinAnimalReq.getUserIdx() == null) {
            return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
        }
        if (joinAnimalReq.getName() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_NAME);
        }
        if (joinAnimalReq.getYear() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_YEAR);
        }
        if (joinAnimalReq.getMonth() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_MONTH);
        }
        if (joinAnimalReq.getGender() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_GENDER);
        }
        if (joinAnimalReq.getSpecies() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_SPECIES);
        }
        if (joinAnimalReq.getMainImgUrl() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_MAINIMG);
        }
        if (joinAnimalReq.getSocialization() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_SOCIALIZATION);
        }
        if (joinAnimalReq.getSeparation() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_SEPARATION);
        }
        if (joinAnimalReq.getToilet() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_TOILET);
        }
        if (joinAnimalReq.getBark() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_BARK);
        }
        if (joinAnimalReq.getBite() == null) {
            return new BaseResponse<>(BaseResponseStatus.ANIMAL_EMPTY_BITE);
        }

        Animal joinAnimal = new Animal();
        joinAnimal.setName(joinAnimalReq.getName());
        joinAnimal.setAge(new Age(joinAnimalReq.getYear(), joinAnimalReq.getMonth(), joinAnimalReq.isGuessed()));
        joinAnimal.setGender(joinAnimalReq.getGender());
        joinAnimal.setSpecies(joinAnimalReq.getSpecies());
        joinAnimal.setMainImgUrl(joinAnimalReq.getMainImgUrl());
        joinAnimal.setIsAdopted(joinAnimalReq.getIsAdopted());
        joinAnimal.setSocialization(joinAnimalReq.getSocialization());
        joinAnimal.setSeparation(joinAnimalReq.getSeparation());
        joinAnimal.setToilet(joinAnimalReq.getToilet());
        joinAnimal.setBark(joinAnimalReq.getBark());
        joinAnimal.setBite(joinAnimalReq.getBite());
        try {
            User findUser = animalService.findUser(joinAnimalReq.getUserIdx());
            joinAnimal.modifyUploadUser(findUser);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
        List<String> illnessList = joinAnimalReq.getIllnessList();
        Long saveAnimalIdx = animalService.saveAnimal(joinAnimal, illnessList);

        return new BaseResponse<>(new JoinAnimalRes(saveAnimalIdx));
    }

    /**
     * 동물 리스트 조회 API
     * @param page
     * @return GetAnimalRes
     */
    @GetMapping("/animals")
    //@ResponseBody
    public BaseResponse<GetAnimalRes> getAnimals(@RequestParam @NotNull Integer page) {

        try {
            GetAnimalRes getAnimalRes = new GetAnimalRes();
            GetAnimalRes animalRes = animalService.getAnimals(page, getAnimalRes);
            return new BaseResponse<>(animalRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 동물 삭제 API
     * @param animal_id
     */
    @DeleteMapping("/auth/organization/animal/{animal_id}")
    public BaseResponse<String> deleteAnimal(@PathVariable Long animal_id) {

        try {
            animalService.deleteAnimal(animal_id);
            String result = animal_id + " 번 동물이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 동물 상세 조회 API
     * @param animal_id
     * @return GetAnimalDetailRes
     */
    @GetMapping("/animals/{animal_id}")
    public BaseResponse<GetAnimalDetailRes> getAnimalDetail(@PathVariable Long animal_id) {

        try {
            GetAnimalDetailRes getAnimalDetailRes = new GetAnimalDetailRes();

            Animal animal = animalService.getAnimal(animal_id);
            getAnimalDetailRes.setName(animal.getName());
            getAnimalDetailRes.setMainImgUrl(animal.getMainImgUrl());
            getAnimalDetailRes.setSpecies(animal.getSpecies());
            getAnimalDetailRes.setAge(animal.getAge());
            getAnimalDetailRes.setGender(animal.getGender());
            getAnimalDetailRes.setIsAdopted(animal.getIsAdopted());
            getAnimalDetailRes.setOrganizationName(animal.getUploadUser().getOrganizationName());
            getAnimalDetailRes.setOrganizationMemberId(animal.getUploadUser().getUsername());
            getAnimalDetailRes.setOrganizationMemberImgUrl(animal.getUploadUser().getProfileImgUrl());
            getAnimalDetailRes.setPhoneNumber(animal.getUploadUser().getPhoneNumber());
            getAnimalDetailRes.setAddress(animal.getUploadUser().getAddress());

            List<Illness> illnessList = animal.getIllnessList();

            for (Illness illness : illnessList) {
                IllnessDto illnessDto = new IllnessDto();
                illnessDto.setIllnessName(illness.getName());
                getAnimalDetailRes.getIllness().add(illnessDto);
            }

            List<Post> postList = postService.getAnimalPost(animal_id);
            for (Post post : postList) {
                PostDto postDto = new PostDto();
                postDto.setPostIdx(post.getIdx());
                postDto.setPostImgUrl(post.getImgUrl());
                getAnimalDetailRes.getPost().add(postDto);
            }

            List<RecommandAnimalDto> recommendAnimals = animalRepository.getRecommendAnimals(animal_id);
            getAnimalDetailRes.setRecommandAnimal(recommendAnimals);

            return new BaseResponse<>(getAnimalDetailRes);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 동물 검색 API
     * @param page
     * @param searchAnimalReq
     * @return GetAnimalRes
     */
    @PostMapping("/search")
    public BaseResponse<GetAnimalRes> searchAnimal(@RequestParam @NotNull Integer page, @RequestBody SearchAnimalReq searchAnimalReq) {

        try {
            /*if (page == null) {
                return new BaseResponse<>(BaseResponseStatus.EMPTY_URL_VALUE);
            }*/
            GetAnimalRes getAnimalRes = animalService.searchAnimal(page, searchAnimalReq, new GetAnimalRes());
            return new BaseResponse<>(getAnimalRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_URL_VALUE);
        }
    }

    /**
     * 입양 버튼 API
     * @param animal_id
     * @return AdoptAnimalRes
     */
    @PatchMapping("/auth/organization/adopt")
    public BaseResponse<AdoptAnimalRes> adoptAnimal(@RequestParam @NotNull Long animal_id) {

        if (animal_id == null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_URL_VALUE);
        }

        try {
            AdoptAnimalRes adoptAnimalRes = animalService.adoptAnimal(animal_id);
            return new BaseResponse<>(adoptAnimalRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 관심 동물 - 즐겨찾기 API
     * @param user_id
     * @param animal_id
     * @return LikeAnimalRes
     */
    @PatchMapping("/auth/private/like")
    public BaseResponse<LikeAnimalRes> likeAnimal(@RequestParam @NotNull Long user_id, @RequestParam @NotNull Long animal_id) {

        LikeAnimalRes likeAnimalRes = animalService.likeAnimal(user_id, animal_id, new LikeAnimalRes());
        return new BaseResponse<>(likeAnimalRes);
    }
}
