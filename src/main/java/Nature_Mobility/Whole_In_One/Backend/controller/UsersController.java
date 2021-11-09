package Nature_Mobility.Whole_In_One.Backend.controller;

import Nature_Mobility.Whole_In_One.Backend.config.BaseException;
import Nature_Mobility.Whole_In_One.Backend.config.BaseResponse;
import Nature_Mobility.Whole_In_One.Backend.domain.users.*;
import Nature_Mobility.Whole_In_One.Backend.service.users.UsersProvider;
import Nature_Mobility.Whole_In_One.Backend.service.users.UsersService;
import Nature_Mobility.Whole_In_One.Backend.utils.JwtService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Nature_Mobility.Whole_In_One.Backend.config.BaseResponseStatus.*;
import static Nature_Mobility.Whole_In_One.Backend.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersProvider usersProvider;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtService jwtService;

    /**
     * 회원 전체 조회 API
     * [GET] /users
     * 회원 닉네임 검색 조회 API
     * [GET] /users?word=
     *
     * @return BaseResponse<List < GetUsersRes>>
     */
    @Operation(summary = "전체 회원 조회 api", description = "전체 회원을 조회하는 api\n" +
            "word 값이 있을 경우 word와 부분적으로 일치하는 닉네임의 회원 정보를 조회합니다."
    )
    @Parameter(name = "word", description = "찾는 닉네임 이름", required = false, in = ParameterIn.QUERY)
    @ApiResponses({
            @ApiResponse(code = 1010, message = "회원 전체 정보 조회에 성공하였습니다."),
            @ApiResponse(code = 1017, message = "회원 검색 조회에 성공하였습니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUsersRes>> getUsers(@RequestParam(required = false) String word) {
        try {
            List<GetUsersRes> getUsersResList = usersProvider.retrieveUserInfoList(word);
            if (word == null) {
                return new BaseResponse<>(SUCCESS_READ_USERS, getUsersResList);
            } else {
                return new BaseResponse<>(SUCCESS_READ_SEARCH_USERS, getUsersResList);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 마이페이지 API
     * [GET] /users/mypage
     * @RequestHeader X-ACCESS-TOKEN
     * @return BaseResponse<GetMyPageRes>
     */
    @Operation(summary = "마이페이지 api", description = "마이페이지 정보를 반환하는 api")
    @ApiResponses({
            @ApiResponse(code = 1011, message = "회원 조회에 성공하였습니다."),
            @ApiResponse(code = 2011, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/mypage")
    public BaseResponse<GetMyPageRes> getMyPage(@RequestHeader("X-ACCESS-TOKEN") String token) {
        Long userIdx;
        try {
            userIdx = jwtService.getUserIdx();
            log.info(userIdx.toString());
            GetMyPageRes getMyPageRes = usersProvider.myPage(userIdx);
            return new BaseResponse<>(SUCCESS_READ_USER, getMyPageRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 개인 정보 조회 API
     * [GET] /users/mypage/edit
     * @RequestHeader X-ACCESS-TOKEN
     * @return BaseResponse<GetUserRes>
     */
    @Operation(summary = "회원 개인 정보를 조회하는 api", description = "마이페이지 정보를 반환하는 api")
    @ApiResponses({
            @ApiResponse(code = 1011, message = "회원 조회에 성공하였습니다."),
            @ApiResponse(code = 2011, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3010, message = "존재하지 않는 회원입니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @ResponseBody
    @GetMapping("/mypage/edit")
    public BaseResponse<GetUserRes> getUser(@RequestHeader("X-ACCESS-TOKEN") String token) {
        try {
            Long userIdx = jwtService.getUserIdx();
            GetUserRes getUserRes = usersProvider.retrieveUserInfo(userIdx);
            return new BaseResponse<>(SUCCESS_READ_USER, getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 회원 정보 수정 API (id는 수정 불가, 비밀번호는 따로 수정)
     * [PATCH] /users/mypage/edit
     * @RequestBody PatchUserReq
     * @RequestHeader X-ACCESS-TOKEN
     * @return BaseResponse<PatchUserRe>
     */
    @Operation(summary = "개인 정보 수정 api", description = "개인 정보를 수정하는 api")
    @ApiResponses({
            @ApiResponse(code = 1011, message = "회원 조회에 성공하였습니다."),
            @ApiResponse(code = 2011, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3010, message = "존재하지 않는 회원입니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @ResponseBody
    @PatchMapping("/mypage/edit")
    public BaseResponse<PatchUserRes> getUser(@RequestBody PatchUserReq parameters ,@RequestHeader("X-ACCESS-TOKEN") String token) {
        try {
            Long userIdx = jwtService.getUserIdx();
            return new BaseResponse<>(SUCCESS_PATCH_USER, usersService.updateUserInfo(userIdx, parameters));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 비밀번호 수정 (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Patch] /users/mypage/edit_password
     * @RequestBody PatchPWReq
     * @return PostCheck
     */
    @Operation(summary = "비밀 번호 수정 api", description = "개인 정보를 수정하는 api")
    @ApiResponses({
            @ApiResponse(code = 1021, message = "비밀번호 변경에 성공했습니다."),
            @ApiResponse(code = 2011, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2032, message = "비밀번호를 다시 입력해주세요."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3010, message = "존재하지 않는 회원입니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다."),
            @ApiResponse(code = 3020, message = "비밀번호 수정에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/mypage/edit_password")
    public BaseResponse<Void> editPW(@RequestBody PatchPWReq patchPWReq, @RequestHeader("X-ACCESS-TOKEN") String token) {
        // 2. Post UserInfo
        PostLoginRes postLoginRes;
        try {
            Long userIdx = jwtService.getUserIdx();
            usersService.editPW(userIdx,patchPWReq);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS_EDIT_PW);
    }
    /**
     * 이메일 중복 확인 API (반드시 회원 가입전 확인)
     * [Post] /users/sign_up/check_userEmail
     * @RequestParam email
     * @return BaseResponse<String>
     */
    @Operation(summary = "이메일 중복 확인 api", description = "중복되는 이메일이 있는지 확인하는 api\n" +
            "회원 가입 전 비동기로 이메일 중복을 확인하기 위한 api")
    @ApiResponses({
            @ApiResponse(code = 1018, message = "이메일 중복 확인에 성공했습니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3017, message = "이미 존재하는 이메일입니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @ResponseBody
    @PostMapping("sign_up/check_userEmail")
    public BaseResponse<String> checkEmail(@RequestParam("email") String userEmail){
        String check ="";
        try {
            check = usersService.checkEmail(userEmail);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        // 2. Post UserInfo
        return new BaseResponse<>(SUCCESS_CHECK_EMAIL,check);
    }

    /**
     * 아이디 중복 확인 API (반드시 회원 가입전 확인)
     * [Post] /users/sign_up/check_userId
     * @RequestParam userId
     * @return BaseResponse<String>
     */
    @Operation(summary = "아이디 중복 확인 api", description = "중복되는 아이디가 있는지 확인하는 api\n" +
            "회원 가입 전 비동기로 이메일 중복을 확인하기 위한 api")
    @ApiResponses({
            @ApiResponse(code = 1019, message = "아이디 중복 확인에 성공했습니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3018, message = "이미 존재하는 아이디입니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @ResponseBody
    @PostMapping("sign_up/check_userId")
    public BaseResponse<String> checkID(@RequestParam("userId") String userId) {
        String check ="";
        try {
            check = usersService.checkID(userId);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        // 2. Post UserInfo
        return new BaseResponse<>(SUCCESS_CHECK_ID,check);
    }

    /**
     * 회원가입 API
     * [POST] /sign_up
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @Operation(summary = "회원가입 api", description = "회원가입 api\n 중복된 아이디, 이메일로 회원가입 불가능")
    @ApiResponses({
            @ApiResponse(code = 1012, message = "회원가입에 성공하였습니다."),
            @ApiResponse(code = 2020, message = "이메일을 입력해주세요."),
            @ApiResponse(code = 2021, message = "이메일 형식을 확인해주세요."),
            @ApiResponse(code = 2030, message = "비밀번호를 입력해주세요."),
            @ApiResponse(code = 2031, message = "비밀번호 확인을 입력해주세요."),
            @ApiResponse(code = 2033, message = "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
            @ApiResponse(code = 2040, message = "닉네임을 입력해주세요."),
            @ApiResponse(code = 2050, message = "아이디를 입력해주세요."),
            @ApiResponse(code = 2060, message = "이름을 입력해주세요."),
            @ApiResponse(code = 3011, message = "이미 존재하는 회원입니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3012, message = "회원가입에 실패하였습니다."),
            @ApiResponse(code = 3018, message = "이미 존재하는 아이디입니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @ResponseBody
    @PostMapping("sign_up")
    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
            return new BaseResponse<>(EMPTY_EMAIL);
        }
        if (!isRegexEmail(parameters.getEmail())){
            return new BaseResponse<>(INVALID_EMAIL);
        }
        if (parameters.getId() == null || parameters.getId().length() == 0) {
            return new BaseResponse<>(EMPTY_USERID);
        }
        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }
        if (parameters.getConfirmPassword() == null || parameters.getConfirmPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_CONFIRM_PASSWORD);
        }
        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
            return new BaseResponse<>(DO_NOT_MATCH_PASSWORD);
        }
        if (parameters.getNickname() == null || parameters.getNickname().length() == 0) {
            return new BaseResponse<>(EMPTY_USERNICKNAME);
        }
        if (parameters.getName() == null || parameters.getName().length() == 0) {
            return new BaseResponse<>(EMPTY_USERNAME);
        }

        // 2. Post UserInfo
        try {
            PostUserRes postUserRes = usersService.createUserInfo(parameters);
            return new BaseResponse<>(SUCCESS_POST_USER, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 비밀번호 확인 API (개인정보 조회 및 수정 전 비밀번호 요구 시)
     * [Post] /users/check_password
     * @RequestParam password
     * @RequestHeader X-ACCESS-TOKEN
     * BaseResponse<PostLoginRes>
     */
    @Operation(summary = "비밀 번호 확인 api", description = "개인정보 조회 등 민감한 정보 조회 또는 변경 시 " +
            "비밀 번호 인증이 다시 필요한 경우 사용\n" +
            "jwt 인증 토큰을 재발급한다.")
    @ApiResponses({
            @ApiResponse(code = 1020, message = "비밀번호 확인에 성공했습니다."),
            @ApiResponse(code = 2011, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2032, message = "비밀번호를 다시 입력해주세요."),
            @ApiResponse(code = 3010, message = "존재하지 않는 회원입니다."),
            @ApiResponse(code = 3014, message = "로그인에 실패하였습니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @ResponseBody
    @PostMapping("check_password")
    public BaseResponse<PostLoginRes> confirmPW(@RequestParam("password") String password,
                                                @RequestHeader("X-ACCESS-TOKEN") String token) {
        // 2. Post UserInfo
        PostLoginRes postLoginRes;
        try {
            Long userIdx = jwtService.getUserIdx();
            postLoginRes = usersProvider.checkPW(userIdx,password);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
        return new BaseResponse<>(SUCCESS_CHECK_PW,postLoginRes);
    }

    //아이디 또는 이메일로 로그인 가능
    /**
     * 로그인 API
     * [POST] /users/login
     * @RequestBody PostLoginReq
     * @return BaseResponse<PostLoginRes>
     */
    @Operation(summary = "로그인 api", description = "아이디 또는 이메일로 로그인 가능")
    @ApiResponses({
            @ApiResponse(code = 1013, message = "로그인에 성공하였습니다."),
            @ApiResponse(code = 2021, message = "이메일 형식을 확인해주세요."),
            @ApiResponse(code = 2030, message = "비밀번호를 입력해주세요."),
            @ApiResponse(code = 2032, message = "비밀번호를 다시 입력해주세요."),
            @ApiResponse(code = 2050, message = "아이디를 입력해주세요."),
            @ApiResponse(code = 3010, message = "존재하지 않는 회원입니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3014, message = "로그인에 실패하였습니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getId() == null || parameters.getId().length() == 0){
            return new BaseResponse<>(EMPTY_USERID);
        } else if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
            return new BaseResponse<>(EMPTY_PASSWORD);
        }

        // 2. Login
        try {
            PostLoginRes postLoginRes = usersProvider.login(parameters);
            return new BaseResponse<>(SUCCESS_LOGIN, postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /users/withdraw
     * @return BaseResponse<Void>
     */
    @Operation(summary = "회원탈퇴 api", description = "회원을 DB에서 바로 삭제하지 않고 status만 변경한다.")
    @ApiResponses({
            @ApiResponse(code = 1015, message = "회원 탈퇴에 성공하였습니다."),
            @ApiResponse(code = 2011, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 3010, message = "존재하지 않는 회원입니다."),
            @ApiResponse(code = 3012, message = "회원 정보 조회에 실패하였습니다."),
            @ApiResponse(code = 3015, message = "회원 탈퇴에 실패하였습니다."),
            @ApiResponse(code = 3019, message = "비활성화된 계정입니다.")
    })
    @DeleteMapping("/withdraw")
    public BaseResponse<Void> deleteUsers(@RequestHeader("X-ACCESS-TOKEN") String token) {
        try {
            Long userIdx = jwtService.getUserIdx();
            usersService.deleteUserInfo(userIdx);
            return new BaseResponse<>(SUCCESS_DELETE_USER);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * JWT 검증 API
     * [GET] /users/jwt
     * @return BaseResponse<Void>
     */
//    @GetMapping("/jwt")
//    public BaseResponse<Void> jwt() {
//        try {
//            Long userIdx = jwtService.getUserIdx();
//            usersProvider.retrieveUserInfo(userIdx);
//            return new BaseResponse<>(SUCCESS_JWT);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}
