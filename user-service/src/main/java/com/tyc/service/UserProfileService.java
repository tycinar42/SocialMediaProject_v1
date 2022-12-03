package com.tyc.service;

import com.tyc.dto.request.ActivateReguestDto;
import com.tyc.dto.request.FindByToken;
import com.tyc.dto.request.NewCreateUserDto;
import com.tyc.dto.request.UpdateRequestDto;
import com.tyc.dto.response.RoleResponseDto;
import com.tyc.dto.response.UserProfileRedisResponseDto;
import com.tyc.exception.ErrorType;
import com.tyc.exception.UserManagerException;
import com.tyc.manager.IAuthManager;
import com.tyc.manager.IElasticManager;
import com.tyc.mapper.IUserMapper;
import com.tyc.rabbitmq.model.UpdateUsernameEmailModel;
import com.tyc.rabbitmq.procedure.UpdateUserProcedure;
import com.tyc.repository.IUserProfileRepository;
import com.tyc.repository.entity.UserProfile;
import com.tyc.repository.enums.Status;
import com.tyc.utility.JwtTokenManager;
import com.tyc.utility.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 1-Status değişdiği zaman bizim active status cache temizlensin
 * 2-Userprofilecontrollerda bir endpoint
 * buda bize dışarıdan girdiğimiz role göre bize user profileları donsün
 * ve bu metodu cachleyelim
 * bu metod ne zaman değişecek yani bu cache bir metodun içinde yeri geldiği zaman silelim
 */
@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {

    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;
    @Autowired
    private CacheManager cacheManager;

    private final IAuthManager authManager;

    private final IElasticManager elasticManager;

    private final UpdateUserProcedure updateUserProcedure;

    public UserProfileService(IUserProfileRepository userProfileRepository,
                              JwtTokenManager jwtTokenManager, IAuthManager authManager,
                              IElasticManager elasticManager, UpdateUserProcedure updateUserProcedure) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.elasticManager = elasticManager;
        this.updateUserProcedure = updateUserProcedure;
    }

    @Transactional
    public UserProfile createUser(NewCreateUserDto dto) {

        try {
            UserProfile userProfile = userProfileRepository.save(IUserMapper.INSTANCE.toUserProfile(dto));
            elasticManager.createUser(IUserMapper.INSTANCE.toUserProfileResponseDto(userProfile));
            return userProfile;
        } catch (Exception e) {

            e.printStackTrace();
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }

    }


    public boolean activateStatus(ActivateReguestDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthid(dto.getId());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(Status.ACTIVE);
        save(userProfile.get());
        return true;

    }

    public boolean updateUser(UpdateRequestDto dto) {

        Optional<Long> authid = jwtTokenManager.getUserId(dto.getToken());
        if (authid.isPresent()) {
            Optional<UserProfile> userProfileDb = userProfileRepository.findOptionalByAuthid(authid.get());
            if (userProfileDb.isPresent()) {
                cacheManager.getCache("findbyusername").evict(userProfileDb.get().getUsername().toUpperCase());
                userProfileDb.get().setEmail(dto.getEmail());
                userProfileDb.get().setAddress(dto.getAddress());
                userProfileDb.get().setAbout(dto.getAbout());
                userProfileDb.get().setName(dto.getName());
                userProfileDb.get().setUsername(dto.getUsername());
                userProfileDb.get().setPhone(dto.getPhone());
                userProfileDb.get().setPhone(dto.getPhoto());
                userProfileDb.get().setUpdated(System.currentTimeMillis());
                save(userProfileDb.get());
                elasticManager.update(IUserMapper.INSTANCE.toUserProfileResponseDto(userProfileDb.get()));
                return true;
            } else {
                throw new UserManagerException(ErrorType.USER_NOT_FOUND);
            }


        } else {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }


    }

    public boolean updateUserForRedis(UpdateRequestDto dto) {

        Optional<Long> authid = jwtTokenManager.getUserId(dto.getToken());
        if (authid.isPresent()) {
            Optional<UserProfile> userProfileDb = userProfileRepository.findOptionalByAuthid(authid.get());
            if (userProfileDb.isPresent()) {
                cacheManager.getCache("findbyusername").evict(userProfileDb.get().getUsername().toUpperCase());
                userProfileDb.get().setEmail(dto.getEmail());
                userProfileDb.get().setAddress(dto.getAddress());
                userProfileDb.get().setAbout(dto.getAbout());
                userProfileDb.get().setName(dto.getName());
                userProfileDb.get().setUsername(dto.getUsername());
                userProfileDb.get().setPhone(dto.getPhone());
                userProfileDb.get().setPhone(dto.getPhoto());
                save(userProfileDb.get());
                return true;
            } else {
                throw new UserManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }


    }

    public boolean updateUserWithRabbitMq(UpdateRequestDto dto) {

        Optional<Long> authid = jwtTokenManager.getUserId(dto.getToken());
        if (authid.isPresent()) {
            Optional<UserProfile> userProfileDb = userProfileRepository.findOptionalByAuthid(authid.get());
            if (userProfileDb.isPresent()) {
                boolean check = chekingUsernameAndEmail(dto, userProfileDb.get());
                cacheManager.getCache("findbyusername").evict(userProfileDb.get().getUsername().toUpperCase());
                userProfileDb.get().setEmail(dto.getEmail());
                userProfileDb.get().setAddress(dto.getAddress());
                userProfileDb.get().setAbout(dto.getAbout());
                userProfileDb.get().setName(dto.getName());
                userProfileDb.get().setUsername(dto.getUsername());
                userProfileDb.get().setPhone(dto.getPhone());
                userProfileDb.get().setPhone(dto.getPhoto());
                save(userProfileDb.get());
                if (check) {
                    updateUserProcedure.sendUpdateUser(UpdateUsernameEmailModel.builder()
                            .email(userProfileDb.get().getEmail())
                            .username(userProfileDb.get().getUsername()).
                            id(userProfileDb.get().getAuthid()).build());
                }
                return true;
            } else {
                throw new UserManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);

        }


    }

    public Boolean chekingUsernameAndEmail(UpdateRequestDto dto, UserProfile userProfile) {


        if (!dto.getUsername().equals(userProfile.getUsername()) || !dto.getEmail().equals(userProfile.getEmail())) {
            return true;
        }

        return false;
    }

    public Boolean activateStatus(Long authid) {

        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthid(authid);
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(Status.ACTIVE);
        save(userProfile.get());
        return true;
    }

    @Cacheable(value = "findbyusername", key = "#username.toUpperCase()")
    public UserProfileRedisResponseDto findByUsername(String username) {

        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByUsernameEqualsIgnoreCase(username);

        if (userProfile.isPresent()) {
            return IUserMapper.INSTANCE.toUserProfileRedisResponseDto(userProfile.get());
        } else {

            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

    }

    @Cacheable(value = "findactiveprofile")
    public List<UserProfile> findAllActiveProfile() {
        return userProfileRepository.getActiveProfile();
    }


    @Cacheable(value = "findbyrole", key = "#roles.toUpperCase()")
    public List<RoleResponseDto> findByRole(String roles) {

        return authManager.findAllByRole(roles).getBody();
    }

    @Transactional
    public boolean deleteUser(Long id) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthid(id);

        if (userProfile.isPresent()) {
            userProfile.get().setStatus(Status.DELETED);
            save(userProfile.get());
            elasticManager.deleteUser(id);
            return true;
        } else {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }


    }

    public Page<UserProfile> findallPage(int pageSize, int pageNumber, String direction, String sortParameter) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortParameter);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return userProfileRepository.findAll(pageable);

    }

    public Slice<UserProfile> findallSlice(int pageSize, int pageNumber, String direction, String sortParameter) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortParameter);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return userProfileRepository.findAll(pageable);

    }


    public Optional<UserProfile> findByAuthId(Long id) {

        return userProfileRepository.findOptionalByAuthid(id);
    }

    public UserProfile findByToken(String token) {
        Optional<Long> authid = jwtTokenManager.getUserId(token);
        if (authid.isPresent()) {
            Optional<UserProfile> userProfile = findByAuthId(authid.get());
            if (userProfile.isPresent()) {
                return userProfile.get();
            } else {
                throw new UserManagerException(ErrorType.USER_NOT_FOUND);
            }

        } else {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }


    }

    public UserProfile findById(String id, FindByToken token) {
        Optional<Long> authId = jwtTokenManager.getUserId(token.getToken());
        if (authId.isPresent()) {

            return findById(id).orElse(null);


        } else {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }

    }

//    public Optional<UserProfile> findById(String id) {
//
//        return userProfileRepository.findById(id);
//    }
}
