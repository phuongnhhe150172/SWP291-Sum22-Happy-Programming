package swp.happyprogramming.services;

import swp.happyprogramming.model.Experience;

import java.util.ArrayList;
import java.util.List;

public interface IExperienceService {
    Experience save(Experience experience);

    ArrayList<Experience> getAllExperienceByID(long id);
}
