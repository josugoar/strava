package es.deusto.ingenieria.sd.strava.server.services;

import java.util.HashMap;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.factory.GatewayFactory;

public class AthleteAppService {

    private static AthleteAppService instance;

    private final Map<String, Athlete> athletes = new HashMap<>();
    private final Map<String, String> passwords = new HashMap<>();

    private AthleteAppService() {
    }

    public static AthleteAppService getInstance() {
        if (instance == null) {
            instance = new AthleteAppService();
        }

        return instance;
    }

    public void register(final String password, final Athlete athlete) throws IllegalArgumentException {
        if (athletes.containsKey(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        athletes.put(athlete.getEmail(), athlete);

        passwords.put(athlete.getEmail(), password);
    }

    public void registerGoogle(final Athlete athlete) throws IllegalArgumentException {
        if (athletes.containsKey(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        if (!GatewayFactory.createGateway("Google").checkEmail(athlete.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }

        athletes.put(athlete.getEmail(), athlete);
    }

    public void registerFacebook(final Athlete athlete) throws IllegalArgumentException {
        if (athletes.containsKey(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        if (!GatewayFactory.createGateway("Facebook").checkEmail(athlete.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }

        athletes.put(athlete.getEmail(), athlete);
    }

    public Athlete login(final String email, final String password) throws IllegalArgumentException {
        if (!athletes.containsKey(email)) {
            throw new IllegalArgumentException("Athlete is not registered!");
        }

        if (passwords.get(email).equals(password)
                || GatewayFactory.createGateway("Google").checkEmailAndPassword(email, password)
                || GatewayFactory.createGateway("Facebook").checkEmailAndPassword(email, password)) {
            return athletes.get(email);
        }

        throw new IllegalArgumentException("Invalid password!");
    }

}
