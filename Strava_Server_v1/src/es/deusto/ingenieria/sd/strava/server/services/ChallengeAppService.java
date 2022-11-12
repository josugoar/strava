package es.deusto.ingenieria.sd.strava.server.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public class ChallengeAppService {

    private Map<Integer, Challenge> state = new HashMap<>();

    public List<Challenge> getActiveChallenges(Athlete athlete) {
        List<Challenge> challenges = new ArrayList<>();
        for (Challenge challenge : athlete.getChallenges()) {
            if (challenge.isActive()) {
                challenges.add(challenge);
            }
        }
        return challenges;

    }

    public boolean createChallenge(Athlete athlete, String name, Date startDate, Date endDate, float distance, Duration time,
            boolean isRunning, boolean isCycling) {
        Challenge challenge = new Challenge();
        challenge.setCycling(isCycling);
        challenge.setDistance(distance);
        challenge.setEndDate(endDate);
        challenge.setName(name);
        challenge.setRunning(isRunning);
        challenge.setStartDate(startDate);
        challenge.setTime(time);

        if (!challenge.checkChallenge()) {
            return false;
        }

        state.put(state.size(), challenge);
        athlete.addChallenge(challenge);

        return true;
    }

    public void acceptChallenge(Athlete athlete, int challengeId) throws IllegalArgumentException {
        Challenge challenge = state.get(challengeId);
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge does not exist!");
        }

        if (athlete.getChallenges().contains(challenge)) {
            throw new IllegalArgumentException("Athlete already in challenge!");
        }

        athlete.addChallenge(challenge);
    }

    public float getChallengeState(int challengeId) throws IllegalArgumentException {
        Challenge challenge = state.get(challengeId);
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge does not exist!");
        }

        Date currentDate = new Date();
        int relation = currentDate.compareTo(challenge.getEndDate());

        if (relation >= 0) {
            return 1;
        }

        return (currentDate.getTime() - challenge.getStartDate().getTime()) / (challenge.getEndDate().getTime() - challenge.getStartDate().getTime());
    }

}
