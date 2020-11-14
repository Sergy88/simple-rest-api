package ru.example.restapi.controller;


import org.springframework.web.bind.annotation.*;
import ru.example.restapi.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("message")
public class RestFullController {
    private AtomicInteger counter = new AtomicInteger(4);
    private List<Map<String, String>> messages = new ArrayList<>() {{
        add(new HashMap<>() {{
            put("id", "1");
            put("text", "First message");
        }});
        add(new HashMap<>() {{
            put("id", "2");
            put("text", "Second message");
        }});
        add(new HashMap<>() {{
            put("id", "3");
            put("text", "Third message");
        }});
    }};

    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getMessageFromDb(id);
    }

    @PostMapping
    public Map<String,String> create(@RequestBody Map<String, String> message){
        message.put("id", String.valueOf(counter.getAndIncrement()));
        messages.add(message);
        return message;
    }

    @PutMapping("{id}")
    public Map<String,String> update(@PathVariable String id, @RequestBody Map<String, String> message){
        Map<String,String> messageFromDb = getMessageFromDb(id);
        messageFromDb.putAll(message);
        return messageFromDb;
    }

    @DeleteMapping({"id"})
    private void deleteMessage(@PathVariable String id){
        Map<String,String> messageFromDb = getMessageFromDb("id");
        messages.remove(messageFromDb);
    }

    private Map<String, String> getMessageFromDb(String id) {
        return messages.stream()
                .filter(m -> m.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
