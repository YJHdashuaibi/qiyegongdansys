package com.example.yaojunhong_demo.controller;

import com.example.yaojunhong_demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Map<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserController() {
        User user1 = new User(idGenerator.getAndIncrement(), "张三", "zhangsan@example.com", 25);
        User user2 = new User(idGenerator.getAndIncrement(), "李四", "lisi@example.com", 30);
        userStore.put(user1.getId(), user1);
        userStore.put(user2.getId(), user2);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStore.values();
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) Integer minAge,
                                  @RequestParam(required = false) Integer maxAge) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        return userStore.values().stream()
                .filter(user -> kw.isEmpty() || containsIgnoreCase(user.getName(), kw) || containsIgnoreCase(user.getEmail(), kw))
                .filter(user -> name == null || containsIgnoreCase(user.getName(), name))
                .filter(user -> email == null || containsIgnoreCase(user.getEmail(), email))
                .filter(user -> minAge == null || user.getAge() >= minAge)
                .filter(user -> maxAge == null || user.getAge() <= maxAge)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userStore.get(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "用户不存在", "id", id));
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setId(idGenerator.getAndIncrement());
        user.setCreatedAt(LocalDateTime.now());
        userStore.put(user.getId(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!userStore.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "用户不存在", "id", id));
        }
        user.setId(id);
        user.setCreatedAt(userStore.get(id).getCreatedAt());
        userStore.put(id, user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User removed = userStore.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "用户不存在", "id", id));
        }
        return ResponseEntity.ok(Map.of("message", "用户删除成功", "id", id));
    }

    private boolean containsIgnoreCase(String source, String target) {
        return source != null && source.toLowerCase().contains(target);
    }
}