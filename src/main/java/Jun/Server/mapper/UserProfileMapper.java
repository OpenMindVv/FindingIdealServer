package Jun.Server.mapper;
import java.io.File;
import java.util.List;
import Jun.Server.model.UserProfile;
import com.google.gson.JsonObject;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface UserProfileMapper {
    @Select("SELECT * FROM UserProfile WHERE email=#{email}")
    UserProfile getUserProfile(@Param("email") String email);

    @Select("SELECT * FROM UserProfile")
    List<UserProfile> getUserProfileList();

    @Select("SELECT * FROM Information WHERE user=#{image}")
    String getUserProfileImage(@Param("image") String image);


    @Select("SELECT * FROM UserProfile WHERE email=#{email} AND password=#{password}")
    UserProfile login(@Param("email") String email, @Param("password") String password);

    @Select("SELECT * FROM UserProfile WHERE email=#{email}")
    UserProfile getPassword(@Param("email") String email);

    @Insert("INSERT INTO UserProfile VALUES(#{email}, #{password}, #{name}, #{follow}, #{following})")
    int insertUserProfile(@Param("email") String email, @Param("password") String password, @Param("name") String name, @Param("follow") String follow, @Param("following") String following);

    // 이미지 저장하는 메서드
    @Insert("INSERT INTO Information (user) VALUES(LOAD_FILE(#{imageFile}))")
    int createUserProfileImage(@Param("imageFile") String imageFile);

    @Update("UPDATE UserProfile SET name=#{name}, password=#{password} WHERE email=#{email}")
    int updateUserProfile(@Param("email") String email, @Param("name") String name, @Param("password") String password);

    @Delete("DELETE FROM UserProfile WHERE email=#{email}")
    int deleteUserProfile(@Param("email") String email);
}
