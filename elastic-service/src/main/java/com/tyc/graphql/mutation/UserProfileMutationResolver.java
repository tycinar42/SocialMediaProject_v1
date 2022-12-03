package com.tyc.graphql.mutation;

import com.tyc.graphql.model.UserProfileInput;
import com.tyc.mapper.IUserMapper;
import com.tyc.service.UserProfileService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMutationResolver implements GraphQLMutationResolver {


private final UserProfileService userProfileService;


public Boolean createUserProfile(UserProfileInput profile){
    try {
        userProfileService.save(IUserMapper.INSTANCE.toUserProfile(profile));
        return  true;
    }catch (Exception e){

        return  false;
    }

}

}
