package Facades;

import BuisnessClasses.Actor;
import Services.ActorService;
import Persistence.ActorDAO;
import Persistence.PostgresFactory;
import java.util.List;

public class ActorFacade {
    private ActorService actorService;

    public ActorFacade() {
        PostgresFactory factory = new PostgresFactory();
        ActorDAO actorDAO = factory.createActorDAO();
        this.actorService = new ActorService(actorDAO);
    }

    public ActorFacade(ActorService actorService) {
        this.actorService = actorService;
    }

    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    public Actor getActorById(int id) {
        return actorService.getActorById(id);
    }

    public List<Actor> searchActorsByName(String name) {
        return actorService.searchByName(name);
    }

    public List<String> getAutocompleteSuggestions(String input) {
        return actorService.getAutocompleteSuggestions(input);
    }
}