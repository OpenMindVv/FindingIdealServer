package Jun.Server.controller;

import java.util.List;
import java.util.Map;

import Jun.Server.jwtService.JwtTokenProvider;
import Jun.Server.mapper.UserProfileMapper;
import Jun.Server.model.User;
import Jun.Server.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserProfileController {

    private UserProfileMapper mapper;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserProfileController(UserProfileMapper mapper, JwtTokenProvider jwtTokenProvider) {
        this.mapper = mapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping("/user/getProfile")
    public UserProfile getUserProfile(@RequestParam("email") String email) {
        UserProfile result = mapper.getUserProfile(email);
        System.out.println(result.getEmail());
        System.out.println(result.getPassword());
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

    //정보수정 코드 넣어야함
    @PostMapping("/user/editProfile")
    public void postUserProfile(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password) {
        mapper.updateUserProfile(email, name, password);
    }

    //로그인인데 토큰 발행 해야함
/*
    @PostMapping("/user/login")
    public JsonObject login(@RequestParam("email") String email, @RequestParam("password") String password) {
        String result = mapper.login(email, password);
        JsonObject jsonObject = new JsonObject();
        if(result.equals(null)){
            // Json형식 만들어서 클라로 송신, 클래스 형식으로 만들어서 json형식으로 보낼 수도 있음
            // 단 클래스에 RestController또는 controller와 responsebody 어노테이션이 있어야함
            jsonObject.addProperty("result", "error");
            jsonObject.addProperty("test", "5");
            return jsonObject;
        }
        jsonObject.addProperty("result", "200");
        jsonObject.addProperty("test", "5");
        return jsonObject;
    }

 */

    @PostMapping("user/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
        UserProfile user = mapper.login(email, password);

        if(user == null){
            System.out.println("실패");
        }
        else System.out.println("성공");

        System.out.println(user.getName() + user.getPassword());
        System.out.println("-----------------------------");
        System.out.println(user.getName() + user.getRoles());

        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }


    //회원삭제넣자
    @DeleteMapping("/user/delete")
    public void deleteUserProfile(@PathVariable("email") String email) {
        mapper.deleteUserProfile(email);
    }
}
