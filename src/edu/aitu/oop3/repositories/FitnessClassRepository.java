package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.FitnessClass;
import java.util.List;

public interface FitnessClassRepository {
    List<FitnessClass> findAll();
    FitnessClass findById(int id);
}
