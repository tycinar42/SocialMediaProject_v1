package com.tyc.controller;

import com.tyc.config.security.JwtTokenManager;
import com.tyc.dto.request.ActivateReguestDto;
import com.tyc.dto.request.LoginRequestDto;
import com.tyc.dto.request.RegisterRequestDto;
import com.tyc.dto.response.AuthListResponseDto;
import com.tyc.dto.response.LoginResponseDto;
import com.tyc.dto.response.RegisterResponseDto;
import com.tyc.dto.response.RoleResponseDto;
import com.tyc.repository.entity.Auth;
import com.tyc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import java.util.List;

import static com.tyc.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
@CrossOrigin(origins = "*")
public class AuthController {


    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;


    @PostMapping(REGISTER)
    @Operation(summary = "Kullanıcı kayıt eden metot")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }


    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {

        return ResponseEntity.ok(authService.login(dto).get());
    }

    @GetMapping(GETTOKEN)
    public String getToken(Long id) {

        return jwtTokenManager.createToken(id);
    }

    @GetMapping(GETIDBYTOKEN)
    public Long getId(String token) {

        return jwtTokenManager.getUserId(token).get();
    }


    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateReguestDto dto) {

        return ResponseEntity.ok(authService.activeteStatus(dto));

    }


    @GetMapping(GETALLAUTH)
    public ResponseEntity<List<Auth>> findAll() {

        return ResponseEntity.ok(authService.findAll());

    }

    @GetMapping("/findallbyactiveandpending")
    public ResponseEntity<List<AuthListResponseDto>> findAllByActiveAndPendingAuth() {

        return ResponseEntity.ok(authService.findAllByActiveAndPendingAuth());
    }

    @GetMapping("/findallbystatusin")
    public ResponseEntity<List<AuthListResponseDto>> findAllByStatusIn() {

        return ResponseEntity.ok(authService.findAllByStatusIn());
    }

    @GetMapping("/redis")
    @Cacheable(value = "redis_example1")
    public String redisExample(String value) {
        return value;
    }


    @GetMapping("/findbyrole/{roles}")
    public ResponseEntity<List<RoleResponseDto>> findAllByRole(@PathVariable String roles) {

        return ResponseEntity.ok(authService.findByRole(roles));

    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<RoleResponseDto> findRoleByAuthId(@PathVariable Long id) {

        return ResponseEntity.ok(authService.findRoleByAuthId(id));

    }

    @DeleteMapping("/delete/{token}")
    public ResponseEntity<Boolean> deleteAuth(@PathVariable String token) {

        return ResponseEntity.ok(authService.deleteAuth(token));
    }

}
