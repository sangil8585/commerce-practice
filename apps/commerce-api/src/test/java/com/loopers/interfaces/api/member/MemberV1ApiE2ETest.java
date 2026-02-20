package com.loopers.interfaces.api.member;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberV1ApiE2ETest {

    private static final String ENDPOINT_SIGNUP = "/api/v1/members/signup";
    private static final String ENDPOINT_ME = "/api/v1/members/me";
    private static final String ENDPOINT_CHANGE_PASSWORD = "/api/v1/members/me/password";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public MemberV1ApiE2ETest(
        TestRestTemplate testRestTemplate,
        DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    // ============================================================
    // 회원가입
    // ============================================================
    @DisplayName("POST /api/v1/members/signup")
    @Nested
    class Signup {

        @DisplayName("유효한 정보로 회원가입하면, 200 OK 와 회원 정보를 반환한다.")
        @Test
        void signupSuccessfully_whenValidInfoIsProvided() {
            // arrange
            MemberV1Dto.SignupRequest request = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Passw0rd!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );

            // act
            ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<MemberV1Dto.SignupResponse>> response =
                testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().loginId()).isEqualTo("loopers01"),
                () -> assertThat(response.getBody().data().name()).isEqualTo("변시영"),
                () -> assertThat(response.getBody().data().email()).isEqualTo("sangil@naver.com")
            );
        }

        @DisplayName("이미 가입된 로그인 ID로 가입하면, 409 CONFLICT 응답을 받는다.")
        @Test
        void throwsConflict_whenLoginIdAlreadyExists() {
            // arrange
            MemberV1Dto.SignupRequest firstRequest = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Passw0rd!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );
            testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(firstRequest),
                new ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>>() {});

            MemberV1Dto.SignupRequest duplicateRequest = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Another1!",
                "김철수",
                "19900101",
                "other@example.com"
            );

            // act
            ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<MemberV1Dto.SignupResponse>> response =
                testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(duplicateRequest), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT)
            );
        }

        @DisplayName("비밀번호가 8자 미만이면, 400 BAD_REQUEST 응답을 받는다.")
        @Test
        void throwsBadRequest_whenPasswordIsTooShort() {
            // arrange
            MemberV1Dto.SignupRequest request = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Short1!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );

            // act
            ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<MemberV1Dto.SignupResponse>> response =
                testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }

        @DisplayName("비밀번호에 생년월일이 포함되면, 400 BAD_REQUEST 응답을 받는다.")
        @Test
        void throwsBadRequest_whenPasswordContainsBirthDate() {
            // arrange
            MemberV1Dto.SignupRequest request = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Pass19950315!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );

            // act
            ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<MemberV1Dto.SignupResponse>> response =
                testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }
    }

    // ============================================================
    // 내 정보 조회
    // ============================================================
    @DisplayName("GET /api/v1/members/me")
    @Nested
    class GetMe {

        @DisplayName("유효한 인증 헤더로 요청하면, 마스킹된 이름과 함께 내 정보를 반환한다.")
        @Test
        void returnsMyInfo_whenValidCredentialsAreProvided() {
            // arrange - 먼저 회원가입
            MemberV1Dto.SignupRequest signupRequest = new MemberV1Dto.SignupRequest(
                "loopers01",
                "password1!",
                "변상일",
                "19930224",
                "sangil@naver.com"
            );
            testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(signupRequest),
                new ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>>() {});

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Loopers-LoginId", "loopers01");
            headers.set("X-Loopers-LoginPw", "password1!");

            // act
            ParameterizedTypeReference<ApiResponse<MemberV1Dto.MemberResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<MemberV1Dto.MemberResponse>> response =
                testRestTemplate.exchange(ENDPOINT_ME, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().loginId()).isEqualTo("loopers01"),
                () -> assertThat(response.getBody().data().name()).isEqualTo("변상*"),
                () -> assertThat(response.getBody().data().birthDate()).isEqualTo("19930224"),
                () -> assertThat(response.getBody().data().email()).isEqualTo("sangil@naver.com")
            );
        }

        @DisplayName("잘못된 비밀번호로 요청하면, 401 UNAUTHORIZED 응답을 받는다.")
        @Test
        void throwsUnauthorized_whenPasswordIsWrong() {
            // arrange - 먼저 회원가입
            MemberV1Dto.SignupRequest signupRequest = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Passw0rd!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );
            testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(signupRequest),
                new ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>>() {});

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Loopers-LoginId", "loopers01");
            headers.set("X-Loopers-LoginPw", "WrongPw1!");

            // act
            ParameterizedTypeReference<ApiResponse<MemberV1Dto.MemberResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<MemberV1Dto.MemberResponse>> response =
                testRestTemplate.exchange(ENDPOINT_ME, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    // ============================================================
    // 비밀번호 수정
    // ============================================================
    @DisplayName("PATCH /api/v1/members/me/password")
    @Nested
    class ChangePassword {

        @DisplayName("유효한 새 비밀번호로 변경하면, 200 OK 응답을 받는다.")
        @Test
        void changesPasswordSuccessfully_whenNewPasswordIsValid() {
            // arrange - 먼저 회원가입
            MemberV1Dto.SignupRequest signupRequest = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Passw0rd!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );
            testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(signupRequest),
                new ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>>() {});

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Loopers-LoginId", "loopers01");
            headers.set("X-Loopers-LoginPw", "Passw0rd!");

            MemberV1Dto.ChangePasswordRequest request = new MemberV1Dto.ChangePasswordRequest(
                "Passw0rd!",
                "NewPassw0rd!"
            );

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<Void>> response =
                testRestTemplate.exchange(ENDPOINT_CHANGE_PASSWORD, HttpMethod.PATCH, new HttpEntity<>(request, headers), responseType);

            // assert
            assertTrue(response.getStatusCode().is2xxSuccessful());
        }

        @DisplayName("현재 비밀번호와 동일한 비밀번호로 변경하면, 400 BAD_REQUEST 응답을 받는다.")
        @Test
        void throwsBadRequest_whenNewPasswordIsSameAsCurrent() {
            // arrange - 먼저 회원가입
            MemberV1Dto.SignupRequest signupRequest = new MemberV1Dto.SignupRequest(
                "loopers01",
                "Passw0rd!",
                "변시영",
                "19950315",
                "sangil@naver.com"
            );
            testRestTemplate.exchange(ENDPOINT_SIGNUP, HttpMethod.POST, new HttpEntity<>(signupRequest),
                new ParameterizedTypeReference<ApiResponse<MemberV1Dto.SignupResponse>>() {});

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Loopers-LoginId", "loopers01");
            headers.set("X-Loopers-LoginPw", "Passw0rd!");

            MemberV1Dto.ChangePasswordRequest request = new MemberV1Dto.ChangePasswordRequest(
                "Passw0rd!",
                "Passw0rd!"
            );

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<Void>> response =
                testRestTemplate.exchange(ENDPOINT_CHANGE_PASSWORD, HttpMethod.PATCH, new HttpEntity<>(request, headers), responseType);

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}
