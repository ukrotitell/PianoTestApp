package com.example.PianoREST.PianoREST.controller;

import com.example.PianoREST.PianoREST.model.User;
import com.example.PianoREST.PianoREST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private int userId;
    private ResponseEntity<Map<Integer, Integer>> responseEntity;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Map<Integer, Integer>> create(@RequestBody User user, HttpServletRequest request) {
        userService.create(user);
        //request.setAttribute("keyId", user.getId());
        userId = user.getId();
        //request.setAttribute("param2", "two");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<Map<Integer, Integer>> read() {
        final Map<Integer, Integer> users = userService.readAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/helper")
    public ModelAndView create(@RequestParam int roomId, @RequestParam boolean entrance, HttpServletRequest request) {

        responseEntity = userService.goToDoor(roomId, entrance, userId);

        request.setAttribute("keyId", userId);
        request.setAttribute("roomId", roomId);
        request.setAttribute("entrance", entrance);
        return new ModelAndView("forward:/redirect");
    }

    @PostMapping("/redirect")
    public RedirectView goToRoom(HttpServletRequest request, final RedirectAttributes redirectAttributes) {

        //request.setAttribute("keyId", userId);
        redirectAttributes.addAttribute("roomId", request.getAttribute("roomId"));
        redirectAttributes.addAttribute("entrance", request.getAttribute("entrance"));
        redirectAttributes.addAttribute("keyId", userId);
        return new RedirectView("check");
    }

    @GetMapping(value = "/check")
    public ResponseEntity<Map<Integer, Integer>> check() {
                return responseEntity;
    }

}
