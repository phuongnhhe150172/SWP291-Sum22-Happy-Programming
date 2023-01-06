package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.model.Experience;

import java.util.ArrayList;

public interface IExperienceService {
    Experience save(Experience experience);

    ArrayList<Experience> getAllExperienceByProfileID(long id);
}
