package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.entities.Hobby;
import com.habbits.maintainer.models.requests.CreateHobbyRequest;
import com.habbits.maintainer.models.response.CreateHobbyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Component
class HobbyDB {
   private static final Map<Integer, Hobby> hobbies = new HashMap<>();

   public boolean hasId(int id) {
       return hobbies.containsKey(id);
   }

   public void insert(int id, Hobby hb) {
       hobbies.put(id, hb);
   }

   public Set<Map.Entry<Integer, Hobby>> getHobbySet(){
       return hobbies.entrySet();
   }
}

@RestController
@RequestMapping("/hobby")
public class HobbyController {

    @Autowired
    private HobbyDB db;

    @Autowired
    private CreateHobbyResponse res;

    @PostMapping("/create")
    public CreateHobbyResponse create(@RequestBody CreateHobbyRequest r) {
        if(!db.hasId(r.getId())) {
            Hobby hobby = new Hobby();
            hobby.setId(r.getId());
            hobby.setTitle(r.getTitle());

            db.insert(r.getId(), hobby);
            res.setStatus(true);
        } else {
            res.setStatus(false);
        };

        res.setId(r.getId());
        return res;
    };

    @GetMapping("/show")
    public List<Hobby> getHobbies() {
        ArrayList<Hobby> allHobbies = new ArrayList<>();
        for(Map.Entry<Integer, Hobby> hb: db.getHobbySet()) {
            allHobbies.add(hb.getValue());
        }
        return allHobbies;
    }
}
