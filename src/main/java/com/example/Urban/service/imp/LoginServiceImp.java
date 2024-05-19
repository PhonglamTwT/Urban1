package com.example.Urban.service.imp;

import com.example.Urban.controller.ManagerController;
import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.LoginDTO;
import com.example.Urban.repository.AccountRepository;
import com.example.Urban.service.JWTUtils;
import com.example.Urban.service.LoginService;
import com.example.Urban.service.TokenCacheService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.HashMap;

@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TokenCacheService tokenCacheService;



    @Override
    public LoginDTO loginJwt(AccountDTO loginRequest) {
        LoginDTO loginDTO = new LoginDTO();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var loginUser  = accountRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String jwt = jwtUtils.generateToken(loginUser);
            tokenCacheService.saveJwtToCache(loginRequest, jwt);
            loginDTO.setToken(jwt);
            loginDTO.setMessage("Successfully Logged In");
            loginDTO.setEmployeeId(loginUser.getEmployee().getId());
            return loginDTO;
        } catch (Exception e) {
            throw new RuntimeException("Employee not found with id: " + e);
        }

    }

//            var jwt = jwtUtils.generateToken(user);
//            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

//            response.setStatusCode(200);
//            response.setToken(jwt);
//
//            response.setEmployee_id(user.getEmployee().getId());

//            //  thiết lập URL hình ảnh
//
//            String filename = user.getEmployee().getImage(); // Giả sử trường image là tên file lưu trữ
//            if (filename != null && !filename.isEmpty()) {
//                String imageUrl = MvcUriComponentsBuilder
//                        .fromMethodName(ManagerController.class, "getFile", filename)
//                        .build()
//                        .toString();
//                response.setImage(imageUrl);
//            }
//            response.setName(user.getEmployee().getName());
//            response.setEmail(user.getEmployee().getEmail());
//            response.setPhone(user.getEmployee().getPhone());
//            response.setGender(user.getEmployee().getGender());
//            response.setAddress(user.getEmployee().getAddress());
//            response.setPosition(user.getEmployee().getPosition());
//            response.setHeadquarter(user.getEmployee().getHeadquarter());
//
////            response.setRole(user.getRole());
//
//            response.setRefreshToken(refreshToken);
//            response.setExpirationTime("24Hrs");
//            response.setMessage("Successfully Logged In");

}
