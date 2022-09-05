package Jun.Server.controller;

import java.io.File;
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


    @GetMapping("/user/getPassword")
    public UserProfile getPassword(@RequestParam("email") String email) {
        UserProfile result = mapper.getUserProfile(email);
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

    //회원가입
    @PutMapping("/user/create")
    public int putUserProfile(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("follow") String follow, @RequestParam("following") String following) {
        int result = mapper.insertUserProfile(email, name, password, follow, following);
        System.out.println("--------------------------");
        System.out.println(result);
        return result;
    }

    @PutMapping("/user/ProfileImage")
    public int createUserProfileImage(@RequestParam("imageFile") String imageFile) {
        int result = mapper.createUserProfileImage(imageFile);
        System.out.println("result:   "+result);
        return result;
    }

    //정보수정 코드 넣어야함
    @PostMapping("/user/editProfile")
    public int postUserProfile(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("password") String password) {
        int result = mapper.updateUserProfile(email, name, password);
        return result;
    }

    @PostMapping("user/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
        UserProfile user = mapper.login(email, password);

        if(user == null){
            System.out.println("실패");
        }
        else System.out.println("성공");

        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles()); // roles에 관리자 또는 유저로 역할 나눠야함 -> 나중에 관리자 페이지 만들 때 회원가입에서 추가해야하는거임
    }

    //회원삭제넣자
    @DeleteMapping("/user/delete")
    public void deleteUserProfile(@PathVariable("email") String email) {
        mapper.deleteUserProfile(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
