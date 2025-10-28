package com.vihanga.moneymanager.controller;

import com.vihanga.moneymanager.dto.AuthDto;
import com.vihanga.moneymanager.dto.ProfileDto;
import com.vihanga.moneymanager.service.AppUserDetailsService;
import com.vihanga.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto){
        ProfileDto registeredProfile = profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token){
        boolean isActive = profileService.activateProfile(token);
        if (isActive){
            return ResponseEntity.ok("Profile is activated successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token is not found or already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto){
        try{
            if (!profileService.isAccountActive(authDto.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message","Account is not active. Please activate the account."
                ));
            }
            Map<String,Object> response = profileService.authenticateAndGenerateToken(authDto);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",e.getMessage()
            ));
        }
    }
}
