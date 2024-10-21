package com.example.lab4service.Services;

import com.example.lab4service.Entity.Kafka.ConfirmDeputyMessage;
import com.example.lab4service.Entity.UserEntity;
import com.example.lab4service.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class UsersService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, ConfirmDeputyMessage> kafkaTemplate;

    public List<UserEntity> GetAllUsers(){
        return userRepository.findAll();
    }
    public UserEntity GetUserById(String login){
        return userRepository.findById(login).get();
    }

    public boolean AddUser(String login,String password,int age){
        if (userRepository.existsById(login))
            return false;
        if(age<0)return false;
        userRepository.save(new UserEntity(login,password,age));
        return true;

    }
    public boolean DeleteUserById(String login){
        if(!userRepository.existsById(login))
            return false;
        userRepository.deleteById(login);
        return true;
    }
    public boolean ChangeUser(String login,String newLogin,String name,int age){
        if(!userRepository.existsById(login))
            return false;
        if(userRepository.existsById(newLogin)&& !(login.equals(newLogin)))
            return false;
        if(age<0) return false;
        userRepository.deleteById(login);
        userRepository.save(new UserEntity(newLogin,name,age));
        return true;
    }
    @KafkaListener(topics = "topic-1")
    public void listener(ConfirmDeputyMessage incMessageInput) {
        try {
            String loginUser = incMessageInput.getValue2();
            if(!userRepository.existsById(loginUser)){
                return;
            }
            UserEntity user = userRepository.findById(loginUser).get();
            userRepository.deleteById(loginUser);
            user.setRegisteredObjects(user.getRegisteredObjects()+1);
            userRepository.save(user);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            kafkaTemplate.send("topic-2",new ConfirmDeputyMessage(incMessageInput.getValue1(),timeStamp));
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }
}
