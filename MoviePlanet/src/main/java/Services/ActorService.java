package Services;

import BuisnessClasses.Actor;
import Persistence.ActorDAO;
import java.util.List;
import java.util.stream.Collectors;

public class ActorService {
    private ActorDAO actorDAO;


    public ActorService() {
        throw new IllegalStateException("Use ActorService(ActorDAO) constructor");
    }

    public ActorService(ActorDAO actorDAO) {
        if (actorDAO == null) {
            throw new IllegalArgumentException("ActorDAO cannot be null");
        }
        this.actorDAO = actorDAO;
    }

    public void setActorDAO(ActorDAO actorDAO) {
        this.actorDAO = actorDAO;
    }

    public List<Actor> getAllActors() {
        return actorDAO.findAll();
    }

    public Actor getActorById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID de l'acteur doit être positif");
        }
        return actorDAO.findById(id);
    }

    public List<Actor> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllActors();
        }
        return actorDAO.searchByName(name.trim());
    }

    public List<String> getAutocompleteSuggestions(String input) {
        if (input == null || input.length() < 2) {
            return List.of();
        }

        return searchByName(input)
                .stream()
                .map(Actor::getName)
                .limit(5)
                .collect(Collectors.toList());
    }

    public boolean validateActorData(Actor actor) {
        if (actor == null) return false;
        if (actor.getName() == null || actor.getName().trim().isEmpty()) return false;
        if (actor.getAge() < 0 || actor.getAge() > 150) return false;
        return true;
    }

    public boolean saveActor(Actor actor) {
        if (!validateActorData(actor)) {
            throw new IllegalArgumentException("Données de l'acteur invalides");
        }
        return actorDAO.save(actor);
    }

    public boolean updateActor(Actor actor) {
        if (!validateActorData(actor)) {
            throw new IllegalArgumentException("Données de l'acteur invalides");
        }
        return actorDAO.update(actor);
    }

    public boolean deleteActor(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID de l'acteur doit être positif");
        }
        return actorDAO.delete(id);
    }
}