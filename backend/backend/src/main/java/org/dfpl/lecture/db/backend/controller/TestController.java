package org.dfpl.lecture.db.backend.controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class TestController {
    @PostMapping(value="/testData")
    public Map<Integer, String> testData(@RequestBody List<String> params){
        Map<Integer, String> data = new HashMap<>();
        data.put(1,"사과");
        data.put(2,"포도");
        data.put(3,"바나나");

        int i = 4;
        for(String param : params){
            data.put(i, param);
            i++;
        }

        return data;
    }

}
