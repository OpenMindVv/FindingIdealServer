package Jun.Server.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import Jun.Server.jwtService.JwtAuthenticationFilter;
import Jun.Server.jwtService.JwtTokenProvider;
import Jun.Server.mapper.UserProfileMapper;
import Jun.Server.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class UserProfileController implements UserDetailsService {

    private UserProfileMapper mapper;
    private JwtTokenProvider jwtTokenProvider;

    JwtAuthenticationFilter tokenProvider = new JwtAuthenticationFilter();

    @Autowired
    public UserProfileController(UserProfileMapper mapper, JwtTokenProvider jwtTokenProvider) {
        this.mapper = mapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/user/getImage")
    public String getUserProfileImage(@RequestParam("email") String email) {
        String result = mapper.getUserProfileImage(email);
        return result;
    }

    @GetMapping("/user/getPassword")
    public UserProfile getPassword(@RequestParam("email") String email) {
        UserProfile result = mapper.getUserProfile(email);
        return result;
    }

    @GetMapping("/user/getName")
    public UserProfile getName(@RequestParam("email") String email) {
        UserProfile result = mapper.getName(email);
        System.out.println("result= "+result);
        return result;
    }

    @GetMapping("/user/getProfile")
    public UserProfile getUserProfile(@RequestHeader("X-AUTH-TOKEN") String token) {
        UserProfile result = mapper.getUserProfile(jwtTokenProvider.getUserPk(token));
        System.out.println("token= "+token);
        System.out.println("result= "+result);
        System.out.println("email= " +result.getEmail());
        System.out.println("passowrd= "+result.getPassword());
        return result;
    }

    @GetMapping("/user/all")
    public List<UserProfile> getUserProfileList() {
        return mapper.getUserProfileList();
    }

    //????????????

    @PutMapping("/user/insert")
    public int createProfile(@RequestParam("Image") String Image, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("name") String name, @RequestParam("follow") String follow, @RequestParam("following") String following, @RequestParam("animalFace") String animalFace) {
        int result = mapper.createProfile(Image, email, password, name, follow, following, animalFace);
        System.out.println("--------------------------");
        System.out.println(result);
        return result;
    }

    @PostMapping("/user/insertImage")
    public int insertImage(@RequestParam("imageFile") String imageFile, @RequestParam("animalFace") String animalFace, @RequestParam("email") String email) {
        byte[] decoded = Base64.getDecoder().decode(imageFile.getBytes());
        String decodedString = new String(decoded);
        int result = mapper.insertImage(imageFile, animalFace, email);
        System.out.println("result" + result);

        return 1;
    }

    //???????????? ?????? ????????????
    @PostMapping("/user/editProfile")
    public int postUserProfile(@RequestParam("image") String image, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password) {
        int result = mapper.updateUserProfile(image, name, email, password);
        return result;
    }

    @PostMapping("user/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
        UserProfile user = mapper.login(email, password);

        if(user == null){
            System.out.println("??????");
        }
        else System.out.println("??????");

        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles()); // roles??? ????????? ?????? ????????? ?????? ???????????? -> ????????? ????????? ????????? ?????? ??? ?????????????????? ????????????????????????
    }

    //??????????????????
    @DeleteMapping("/user/delete")
    public void deleteUserProfile(@PathVariable("email") String email) {
        mapper.deleteUserProfile(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
