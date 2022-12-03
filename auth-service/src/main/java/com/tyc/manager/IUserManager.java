package com.tyc.manager;
import static com.tyc.constant.ApiUrls .*;
import com.tyc.dto.request.ActivateReguestDto;
import com.tyc.dto.request.NewCreateUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${myapplication.feign.user}/user",name = "user-service-userprofile",decode404 = true)
public interface IUserManager {

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserDto dto);
    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateReguestDto dto);

    @PostMapping(ACTIVATESTATUSBYID)
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authid);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id);

}
