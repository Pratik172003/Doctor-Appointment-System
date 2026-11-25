package com.example.doctorappointment.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.doctorappointment.Domain.CategoryModel;
import com.example.doctorappointment.Domain.DoctorsModel;
import com.example.doctorappointment.Repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel{

    private final MainRepository repository;

    public MainViewModel(){
        this.repository = new MainRepository();
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        return repository.loadCategory();
    }
    public LiveData<List<DoctorsModel>> loadDoctors(){
        return repository.loadDoctor();
    }
}
