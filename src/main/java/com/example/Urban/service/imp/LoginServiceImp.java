package com.example.Urban.service.imp;

import com.example.Urban.controller.ManagerController;
import com.example.Urban.dto.ReqRes;
import com.example.Urban.repository.AccountRepository;
import com.example.Urban.service.JWTUtils;
import com.example.Urban.service.LoginService;
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

    @Override
    public ReqRes loginJwt(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var user = accountRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);


            response.setEmployee_id(user.getEmployee().getId());

            //  thiết lập URL hình ảnh

                String filename = user.getEmployee().getImage(); // Giả sử trường image là tên file lưu trữ
                if (filename != null && !filename.isEmpty()) {
                    String imageUrl = MvcUriComponentsBuilder
                            .fromMethodName(ManagerController.class, "getFile", filename)
                            .build()
                            .toString();
                    response.setImage(imageUrl);
                }


            response.setName(user.getEmployee().getName());
            response.setEmail(user.getEmployee().getEmail());
            response.setPhone(user.getEmployee().getPhone());
            response.setGender(user.getEmployee().getGender());
            response.setAddress(user.getEmployee().getAddress());
            response.setPosition(user.getEmployee().getPosition());
            response.setPosition(user.getEmployee().getHeadquarter());
            response.setRole(user.getRole());

            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
        }
        return response;
    }
}
