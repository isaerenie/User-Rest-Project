package com.works.services;

import com.works.entities.User;
import com.works.entities.UserPassword;
import com.works.repositories.UserRepository;
import com.works.utils.ERest;
import com.works.utils.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity update(String name, int uid) {
        Map<String, Object> hm = new LinkedHashMap<>();
        hm.put("Result", userRepository.updateNameByUid(name, uid));
        return new ResponseEntity(hm, HttpStatus.OK);
    }

    public ResponseEntity list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.result, userRepository.findAll());
        return new ResponseEntity(hm, HttpStatus.OK);
    }

    public ResponseEntity save(User user) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.result, userRepository.save(user));
        return new ResponseEntity(hm, HttpStatus.OK);
    }

    public ResponseEntity update(User user) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        Optional<User> optionalUser = userRepository.findById(user.getUid());
        if (optionalUser.isPresent()) {
            userRepository.saveAndFlush(user);
            hm.put(ERest.result, user);
            hm.put(ERest.status, true);
        } else {
            hm.put(ERest.message, "Failed");
            hm.put(ERest.status, false);
        }
        return new ResponseEntity(hm, HttpStatus.OK);
    }

    public ResponseEntity delete(String id) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            int iid = Integer.parseInt(id);
            userRepository.deleteById(iid);
            hm.put(ERest.status, true);
        } catch (Exception e) {
            hm.put(ERest.result, id);
            hm.put(ERest.status, false);
        }
        return new ResponseEntity(hm, HttpStatus.OK);
    }

    public ResponseEntity searchU(String id) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            int iid = Integer.parseInt(id);
            Optional<User> optionalUser = userRepository.findById(iid);
            if (optionalUser.isPresent()) {
                hm.put(ERest.result, optionalUser.get());
                hm.put(ERest.status, true);
            } else {
                hm.put(ERest.result, "Not found");
                hm.put(ERest.status, false);
            }
        } catch (Exception e) {
            hm.put(ERest.message, "id request :" + id);
            hm.put(ERest.status, false);
            return new ResponseEntity(hm, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(hm, HttpStatus.OK);
    }

    public ResponseEntity searchNS(String q) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        List<User> lUser = userRepository.findByNameContainsOrSurnameContainsAllIgnoreCase(q, q);
        hm.put(ERest.result, lUser);
        hm.put(ERest.status, true);
        return new ResponseEntity(hm, HttpStatus.OK);
    }
    public ResponseEntity login(User user) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        String newPassWord = Util.md5(user.getPassword());
         Optional<User> optionalUser=userRepository.findByEmailEqualsAndPasswordEquals(user.getEmail(), newPassWord);
        if (optionalUser.isPresent()) {
            hm.put(ERest.status, true);
            hm.put(ERest.message, "Login success");
            User u=optionalUser.get();
            u.setPassword("true");
            hm.put(ERest.result,u);
        }else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Login failed");
        }
        return new ResponseEntity(hm, HttpStatus.OK);
    }
    public ResponseEntity profileUpdate( User user ) {
        Map<String, Object> hm = new LinkedHashMap<>();
        Optional<User> oUser = userRepository.findById(user.getUid());
        if (oUser.isPresent() ) {
            User dbUser = oUser.get();
            dbUser.setName(user.getName());
            dbUser.setAge( user.getAge() );
            dbUser.setEmail(user.getEmail());
            dbUser.setSurname(user.getSurname());
            userRepository.saveAndFlush(dbUser);
            dbUser.setPassword("secur");
            hm.put("status", true);
            hm.put("result", dbUser);
        }else {
            hm.put("status", false);
            hm.put("result", user);
        }
        return new ResponseEntity(hm, HttpStatus.OK);
    }
    public ResponseEntity passwordChange(UserPassword userPassword) {
        Map<String, Object> hm = new LinkedHashMap<>();
        Optional<User> oUser = userRepository.findById( userPassword.getUid() );
        if ( oUser.isPresent() ) {
            User dbUser = oUser.get();
            String dbOldPassword = dbUser.getPassword();
            String jsonOldPassword = Util.md5( userPassword.getOldPassword() );
            if ( dbOldPassword.equals( jsonOldPassword ) ) {
                String jsonNewPassword = Util.md5( userPassword.getNewPassword() );
                dbUser.setPassword( jsonNewPassword );
                userRepository.saveAndFlush( dbUser );
                hm.put("status", true);
                dbUser.setPassword("secur");
                hm.put("result", dbUser);
            }else {
                hm.put("status", false);
                hm.put("result", userPassword);
            }
        }else {
            hm.put("status", false);
            hm.put("result", userPassword);
        }
        return new ResponseEntity(hm, HttpStatus.OK);
    }
}
