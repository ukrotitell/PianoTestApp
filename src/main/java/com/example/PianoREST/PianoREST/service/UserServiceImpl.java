package com.example.PianoREST.PianoREST.service;

import com.example.PianoREST.PianoREST.model.Room;
import com.example.PianoREST.PianoREST.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<Integer, Integer> CLIENT_REPOSITORY_MAP = new HashMap<>();
    private Logger logger = Logger.getLogger(getClass().getName());
    // Переменная для генерации ID клиента
    private static final AtomicInteger CLIENT_ID_HOLDER = new AtomicInteger();
    ArrayList<Room> rooms = new ArrayList<>() {{
        add(new Room(1));
        add(new Room(2));
        add(new Room(3));
        add(new Room(4));
        add(new Room(5));
    }};

    @Override
    public void create(User user) {
        final int userId = CLIENT_ID_HOLDER.incrementAndGet();
        user.setId(userId);
        logger.info("Created user with id: " + user.getId());
        CLIENT_REPOSITORY_MAP.put(userId, null);
    }

    @Override
    public Map<Integer, Integer> readAll() {
        return CLIENT_REPOSITORY_MAP;
    }

    @Override
    public ResponseEntity<Map<Integer, Integer>> goToDoor(int roomId, boolean entrance, int userId) {
        if (roomId > rooms.size() || roomId < 0) {
            logger.info("Неправильный номер двери(доступно только от 1-5), " + HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userId == 0) {
            logger.info("Пользователь не создан, " + HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (CLIENT_REPOSITORY_MAP.get(userId) == null && !entrance) {
            logger.info("Неоткуда выходить, " + HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (CLIENT_REPOSITORY_MAP.get(userId) == null && entrance && userId % roomId == 0) {
            CLIENT_REPOSITORY_MAP.put(userId, roomId);
            logger.info("Пользователь с id: " + userId + " вошел в комнату " + roomId + ", " + HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (userId % roomId != 0) {
            logger.info("Пользователь с id: " + userId + " не может войти в комнату " + roomId + ", " + HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (CLIENT_REPOSITORY_MAP.get(userId) != null && entrance) {
            logger.info("Пользователь уже в комнате, " + HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (CLIENT_REPOSITORY_MAP.get(userId) == roomId && !entrance) {
            CLIENT_REPOSITORY_MAP.put(userId, null);
            logger.info("Пользователь с id: " + userId + " вышел из комнаты " + roomId + ", " + HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (CLIENT_REPOSITORY_MAP.get(userId) != roomId && !entrance) {
            CLIENT_REPOSITORY_MAP.put(userId, null);
            logger.info("Пользователь с id: " + userId + " не может выйти из другой комнаты, "  + HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
