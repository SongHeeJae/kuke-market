package kukekyakya.kukemarket.dto.sign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "로그인 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @ApiModelProperty(value = "이메일", notes = "사용자의 이메일을 입력해주세요", required = true, example = "member@email.com")
    @Email(message = "{signInRequest.email.email}")
    @NotBlank(message = "{signInRequest.email.notBlank}")
    private String email;

    @ApiModelProperty(value = "비밀번호", notes = "사용자의 비밀번호를 입력해주세요.", required = true, example = "123456a!")
    @NotBlank(message = "{signInRequest.password.notBlank}")
    private String password;
}