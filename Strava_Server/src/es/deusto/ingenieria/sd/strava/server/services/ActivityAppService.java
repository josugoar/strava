package es.deusto.ingenieria.sd.strava.server.services;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.dao.DAO;
import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

public class ActivityAppService {

    private static ActivityAppService instance;

    private ActivityAppService() {
    }

    public static ActivityAppService getInstance() {
        if (instance == null) {
            instance = new ActivityAppService();
        }

        return instance;
    }

    public void createActivity(final Athlete athlete, final Activity activity) throws IllegalArgumentException {
        activity.setEmail(athlete.getEmail());
        if (!DAO.getInstance().containsActivity(activity.getName(), activity.getEmail())) {
            System.out.println("\n\nResult of if: " + DAO.getInstance().containsActivity(activity.getName(), activity.getEmail()) + activity.getEmail() + " "  + activity.getName() + "\n\n");
            athlete.addActivity(activity);
            DAO.getInstance().storeAthlete(athlete);
        }
        System.out.println("\n\nActivities List: " + athlete.getActivities() + "\n\n");
 
    }

    public List<Activity> getActivities(final Athlete athlete) {
        return List.copyOf(athlete.getActivities());
    }

}
