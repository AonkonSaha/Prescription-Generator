package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.enums.Gender;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.repository.UserRepo;
import com.example.prescription_generator.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class UserValidationServiceImp implements UserValidationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isEmptyUserName(String name) {
        return name==null || name.isEmpty();
    }
    @Override
    public boolean isExitUserByContact(String contact) {
        return userRepo.existsByContact(contact);
    }

    @Override
    public boolean isEmptyUserContact(String contact) {
        return contact==null || contact.isEmpty();
    }

    @Override
    public boolean isValidUserContactLength(String contact) {
        return contact.length() == 11;
    }

    @Override
    public boolean isValidUserPasswordLength(String password) {
        return password.length() >= 8;
    }

    @Override
    public boolean isValidEmailFormat(String email) {
        final String EMAIL_REGEX = "^(?!.*\\.{2})([A-Za-z0-9._%+-]+)@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isValidGender(String gender) {
        return Gender.MALE.toString().equalsIgnoreCase(gender) || Gender.FEMALE.toString().equalsIgnoreCase(gender) || Gender.OTHER.toString().equalsIgnoreCase(gender);
    }
    @Override
    public boolean isExitUserById(Long id) {
        return userRepo.existsById(id);
    }

    @Override
    public boolean isExitUserPassword(String contact,String password) {
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if(mUser.isEmpty()){
            return false;
        }
        return passwordEncoder.matches(password,mUser.get().getPassword());
    }
    @Override
    public boolean isEqualNewPassAndConfirmPass(String newPass, String confirmPass){
        return newPass.equals(confirmPass) ;
    }

    @Override
    public boolean isEmptyDoctorDesignation(String designation) {
        return designation==null || designation.trim().isEmpty() ;
    }
    @Override
    public boolean isEmptyDoctorDegrees(Set<String> degrees) {
        return degrees==null || degrees.isEmpty();
    }

    @Override
    public boolean isValidDoctorAges(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return  age>=25 && age<=120;
    }

    @Override
    public boolean isEmptyLincenseNumber(String licenseNumber) {
        return licenseNumber==null  || licenseNumber.isEmpty();
    }

    @Override
    public boolean isValidUserContactDigit(String mobileNumber) {
        for(int i=0;i<mobileNumber.length();i++){
            if(mobileNumber.charAt(i) >= '0' && mobileNumber.charAt(i) <= '9'){
                continue;
            }
            return false;
        }
        return true;
    }


}

