package com.example.PianoREST.PianoREST.service;

import com.example.PianoREST.PianoREST.model.User;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface UserService {
    void create(User user);

    Map<Integer, Integer> readAll();

    ResponseEntity<Map<Integer, Integer>> goToDoor(int roomId, boolean entrance, int userId);
}
