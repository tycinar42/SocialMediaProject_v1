package com.tyc.service;

import com.tyc.config.security.JwtTokenManager;
import com.tyc.dto.request.ActivateReguestDto;
import com.tyc.dto.request.LoginRequestDto;
import com.tyc.dto.request.NewCreateUserDto;
import com.tyc.dto.request.RegisterRequestDto;
import com.tyc.dto.response.AuthListResponseDto;
import com.tyc.dto.response.LoginResponseDto;
import com.tyc.dto.response.RegisterResponseDto;
import com.tyc.dto.response.RoleResponseDto;
import com.tyc.exception.AuthManagerException;
import com.tyc.exception.ErrorType;
import com.tyc.manager.IUserManager;
import com.tyc.mapper.IAuthMapper;
import com.tyc.rabbitmq.model.UpdateUsernameEmailModel;
import com.tyc.rabbitmq.procuder.ActivatedCodeProcedure;
import com.tyc.repository.IAuthRepository;
import com.tyc.repository.entity.Auth;
import com.tyc.repository.enums.Roles;
import com.tyc.repository.enums.Status;
import com.tyc.utility.CodeGenaretor;
import com.tyc.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 1-ActiveterequstDto -> id ,activateCode
 * - booelaan dönüslü bir end point (ActiveterequstDto )
 * servicede gelen dto dan kontroller yapacağız
 * databasede bu ıdli kullanıcı varmı
 * varsa gondrdiğimiz code ile databasede ki kod aynı mı
 */
@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final IAuthRepository authRepository;
    private final IUserManager userManager;

    private final CacheManager cacheManager;

    private JwtTokenManager jwtTokenManager;

    private ActivatedCodeProcedure procedure;


    public AuthService(IAuthRepository authRepository, IUserManager userManager, JwtTokenManager jwtTokenManager, CacheManager cacheManager, ActivatedCodeProcedure procedure) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenManager = jwtTokenManager;
        this.cacheManager = cacheManager;
        this.procedure = procedure;
    }

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {

        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);


//        if (userIsExist(dto.getUsername())){
//            throw  new AuthManagerException(ErrorType.USERNAME_DUPLICATE);
//        }else {
//
//            if (dto.getAdminCode()!=null&& dto.getAdminCode().equals("admin"))  {
//                auth.setRole(Roles.ADMIN);
//            }
//            try {
//                return   save(auth);
//            }catch (Exception e){
//                throw  new AuthManagerException(ErrorType.USER_NOT_CREATED);
//            }
//
//        }
        if (dto.getAdminCode() != null && dto.getAdminCode().equals("admin")) {
            auth.setRole(Roles.ADMIN);
        }
        try {
            auth.setActivatedCode(CodeGenaretor.generateCode(UUID.randomUUID().toString()));

            save(auth);
            cacheManager.getCache("findbyrole").evict(auth.getRole());
            userManager.createUser(NewCreateUserDto.builder()
                    .authid(auth.getId())
                    .email(auth.getEmail())
                    .username(auth.getUsername())
                    .build());

            procedure.sendActivatedCode(
                    com.tyc.rabbitmq.model.ActivateReguestDto.
                            builder().email(auth.getEmail())
                            .activatedCode(auth.getActivatedCode()).build());

            return IAuthMapper.INSTANCE.toRegisterResponseDto(auth);

        } catch (Exception e) {
//                 delete(auth);
            e.printStackTrace();
            throw new AuthManagerException(ErrorType.USER_NOT_CREATED);

        }


    }


    public boolean userIsExist(String username) {

        return authRepository.existUserName(username);
    }

    public Optional<LoginResponseDto> login(LoginRequestDto dto) {
        Optional<Auth> auth = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());

        if (auth.isPresent()) {
            LoginResponseDto loginResponseDto = IAuthMapper.INSTANCE.toLoginResponseDto(auth.get());
            String token = jwtTokenManager.createToken(loginResponseDto.getId());
            loginResponseDto.setToken(token);
            loginResponseDto.setCode(200L);
            return Optional.of(loginResponseDto);
        } else {
            throw new AuthManagerException(ErrorType.LOGIN_ERROR_WRONG);
        }
    }

    public boolean activeteStatus(ActivateReguestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getActivatedCode().equals(dto.getActivatedCode())) {
            auth.get().setStatus(Status.ACTIVE);
            userManager.activateStatus(dto.getId());
            save(auth.get());
            cacheManager.getCache("findactiveprofile").clear();
            return true;
        }

        throw new AuthManagerException(ErrorType.INVALID_ACTİVATE_CODE);


    }

    @Cacheable(value = "redis_example")
    public String redisExample(String value) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value;
    }


    public List<RoleResponseDto> findByRole(String roles) {

        Roles roles1 = null;
        try {
            roles1 = Roles.valueOf(roles.toUpperCase());
        } catch (Exception e) {

            throw new AuthManagerException(ErrorType.ROLE_NOT_FOUND);
        }

        return authRepository.findAllByRole(roles1).stream().
                map(x ->
                        IAuthMapper.INSTANCE.toRoleResponseDto(x)


                ).collect(Collectors.toList());
    }


    public RoleResponseDto findRoleByAuthId(Long id) {

        return IAuthMapper.INSTANCE.toRoleResponseDto(authRepository.findById(id).get());
    }

    @Transactional
    public boolean deleteAuth(String token) {
        Optional<Long> authId = jwtTokenManager.getUserId(token);
        if (authId.isEmpty()) throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        Optional<Auth> auth = authRepository.findById(authId.get());
        if (auth.isEmpty()) throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        try {
            auth.get().setStatus(Status.DELETED);
            save(auth.get());
            userManager.deleteUser(authId.get());
            return true;
        } catch (Exception e) {
            throw new AuthManagerException(ErrorType.USER_NOT_DELETED);
        }
    }

    public List<AuthListResponseDto> findAllByActiveAndPendingAuth() {
        return IAuthMapper.INSTANCE.tAuthListResponseDtoList(authRepository.findAllActiveAndPendingAuth().get());
    }

    public List<AuthListResponseDto> findAllByStatusIn() {
        List<Status> statusList = List.of(Status.ACTIVE, Status.PENDING);
        return IAuthMapper.INSTANCE.tAuthListResponseDtoList(authRepository.findAllByStatusIn(statusList));
    }


    public boolean updateAuth(UpdateUsernameEmailModel model) {

        Optional<Auth> auth = authRepository.findById(model.getId());

        if (auth.isPresent()) {
            auth.get().setEmail(model.getEmail());
            auth.get().setUsername(model.getUsername());
            save(auth.get());
            return true;
        } else {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }


    }
}
