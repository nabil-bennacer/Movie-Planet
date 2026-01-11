package Persistence;

import BuisnessClasses.Actor;
import java.util.List;

public interface ActorDAO {
    List<Actor> findAll();
    Actor findById(int id);
    List<Actor> searchByName(String name);
    boolean save(Actor actor);
    boolean update(Actor actor);
    boolean delete(int id);
}