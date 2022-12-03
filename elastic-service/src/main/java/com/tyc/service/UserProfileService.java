package com.tyc.service;


import com.tyc.exception.ElasticManagerException;
import com.tyc.exception.ErrorType;
import com.tyc.rabbitmq.model.UpdateUsernameEmailModel;
import com.tyc.repository.IUserProfileRepository;
import com.tyc.repository.entity.UserProfile;
import com.tyc.repository.enums.Status;
import com.tyc.graphql.mutation.utility.ServiceManager;
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
public class UserProfileService extends ServiceManager<UserProfile, Long> {

    private final IUserProfileRepository userProfileRepository;


    public UserProfileService(IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;

    }


    public List<UserProfile> findAllContainingUsername(String username) {


        return userProfileRepository.findByUsernameContainingIgnoreCase(username);
    }

    public List<UserProfile> findAllByStatus(String status) {

        return userProfileRepository.findAllByStatus(Status.valueOf(status));
    }

    public List<UserProfile> findAllContainingEmail(String email) {

        return userProfileRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional
    public boolean deleteUser(Long id) {

        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthid(id);

        if (userProfile.isPresent()) {
            userProfile.get().setStatus(Status.DELETED);
            save(userProfile.get());
            return true;
        } else {
            throw new ElasticManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public boolean updateUser(UpdateUsernameEmailModel model) {

        Optional<UserProfile> auth = userProfileRepository.findOptionalByAuthid(model.getId());

        if (auth.isPresent()) {
            auth.get().setEmail(model.getEmail());
            auth.get().setUsername(model.getUsername());
            save(auth.get());
            return true;
        } else {
            throw new ElasticManagerException(ErrorType.USER_NOT_FOUND);
        }
    }
}
