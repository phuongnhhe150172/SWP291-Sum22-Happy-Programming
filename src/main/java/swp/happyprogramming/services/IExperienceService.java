package swp.happyprogramming.services;

import swp.happyprogramming.model.Experience;

import java.util.ArrayList;

public interface IExperienceService {
    Experience save(Experience experience);

    ArrayList<Experience> getAllExperienceByProfileID(long id);
}
