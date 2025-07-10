package umc.demoday.whatisthis.global;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "이게 뭐예요 스프링 서버입니다!";
    }
}