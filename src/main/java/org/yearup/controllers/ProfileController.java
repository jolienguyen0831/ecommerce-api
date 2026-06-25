package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.util.Objects;

@RestController
@RequestMapping("profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private final ProfileService profileService;
    private final UserService userService;


    @Autowired
    public ProfileController(ProfileService profileService, UserService userService)
    {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public Profile getProfile()
    {
        String username;
        username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userService.getUserByUserName(username);
        return profileService.getProfileByUserId(user.getId());
    }
    @PutMapping
    public Profile updateProfile( @RequestBody Profile profile)
    {
        String username;
        username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userService.getUserByUserName(username);
        return profileService.updateProfile(user.getId(), profile);
    }
}
