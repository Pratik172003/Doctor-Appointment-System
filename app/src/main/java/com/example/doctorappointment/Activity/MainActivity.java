
package com.example.doctorappointment.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doctorappointment.Adapter.CategoryAdapter;
import com.example.doctorappointment.Adapter.TopDoctorsAdapter;
import com.example.doctorappointment.Domain.DoctorsModel;
import com.example.doctorappointment.ViewModel.MainViewModel;
import com.example.doctorappointment.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    private List<DoctorsModel> allDoctors = new ArrayList<>();

    private TopDoctorsAdapter doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new MainViewModel();

        setupButtons();
        setupSearch();
        initCategory();
        initTopDoctors();
    }

    // BUTTONS
    private void setupButtons() {

        binding.myAppointmentsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    MyAppointmentsActivity.class
            );
            startActivity(intent);
        });

        binding.logoutBtn.setOnClickListener(v -> logoutUser());
    }

    // SEARCH
    private void setupSearch() {

        binding.editTextText.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        filterDoctors(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
    }

    // FILTER DOCTORS
    private void filterDoctors(String searchText) {

        String query = normalize(searchText);

        List<DoctorsModel> filteredDoctors = new ArrayList<>();

        for (DoctorsModel doctor : allDoctors) {

            if (doctor == null) continue;

            String name = normalize(doctor.getName());
            String specialization = normalize(doctor.getSpecial());
            String address = normalize(doctor.getAddress());
            String biography = normalize(doctor.getBiography());
            String location = normalize(doctor.getLocation());

            // Search in all doctor details
            String searchableText = name + " "
                    + specialization + " "
                    + address + " "
                    + biography + " "
                    + location;

            // Empty search: show all doctors
            if (query.isEmpty()) {
                filteredDoctors.add(doctor);
                continue;
            }

            // Direct doctor details search
            boolean directMatch = searchableText.contains(query);

            // Disease / symptoms search
            boolean diseaseMatch = matchesDisease(
                    query,
                    specialization
            );

            // Multiple words search
            boolean wordsMatch = matchesAllWords(
                    query,
                    searchableText
            );

            if (directMatch || diseaseMatch || wordsMatch) {
                filteredDoctors.add(doctor);
            }
        }

        showDoctors(filteredDoctors);
    }

    // SHOW SEARCH RESULTS
    private void showDoctors(List<DoctorsModel> doctors) {

        binding.doctorView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        doctorAdapter = new TopDoctorsAdapter(doctors);

        binding.doctorView.setAdapter(doctorAdapter);
    }

    // NORMALIZE SEARCH TEXT
    private String normalize(String text) {

        if (text == null) {
            return "";
        }

        return text.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ");
    }

    // MULTIPLE WORD SEARCH

    private boolean matchesAllWords(
            String query,
            String searchableText) {

        if (query == null || query.trim().isEmpty()) {
            return false;
        }

        String[] words = query.trim().split("\\s+");

        int totalWords = 0;
        int matchedWords = 0;

        for (String word : words) {

            if (word.length() < 2) {
                continue;
            }

            totalWords++;

            if (searchableText.contains(word)) {
                matchedWords++;
            }
        }

        return totalWords > 0 &&
                matchedWords == totalWords;
    }


    // DISEASE TO SPECIALIZATION MATCH
    private boolean matchesDisease(
            String query,
            String specialization) {

        String[][] groups = {

                // Heart
                {
                        "heart", "cardiac", "cardiology",
                        "cardiologist"
                },

                // Diabetes
                {
                        "diabetes", "diabetic", "sugar",
                        "diabetologist", "endocrinologist"
                },

                // Thyroid
                {
                        "thyroid", "hormone", "endocrinology"
                },

                // Skin
                {
                        "skin", "acne", "pimple", "rash",
                        "dermatologist", "dermatology"
                },

                // Bone
                {
                        "bone", "joint", "fracture",
                        "orthopedic", "orthopaedic",
                        "orthopedics"
                },

                // Back pain
                {
                        "back pain", "spine", "backache"
                },

                // Child
                {
                        "child", "children", "baby",
                        "pediatric", "paediatric",
                        "pediatrician"
                },

                // Eye
                {
                        "eye", "eyes", "vision",
                        "ophthalmologist", "ophthalmology"
                },

                // Dental
                {
                        "tooth", "teeth", "dental",
                        "dentist"
                },

                // Brain / Nerves
                {
                        "brain", "nerve", "migraine",
                        "neurologist", "neurology"
                },

                // Kidney
                {
                        "kidney", "nephrologist",
                        "nephrology"
                },

                // Stomach
                {
                        "stomach", "gastric", "digestion",
                        "gastroenterologist",
                        "gastroenterology"
                },

                // Acidity
                {
                        "acidity", "acid reflux", "ulcer"
                },

                // Lungs
                {
                        "lung", "asthma", "breathing",
                        "pulmonologist", "pulmonology"
                },

                // Urinary
                {
                        "urine", "bladder", "urologist",
                        "urology"
                },

                // Cancer
                {
                        "cancer", "tumor", "oncologist",
                        "oncology"
                },

                // Women's health
                {
                        "pregnancy", "pregnant", "women",
                        "gynecologist", "gynaecologist",
                        "obstetrician"
                },

                // ENT
                {
                        "ear", "nose", "throat", "ent",
                        "otolaryngologist"
                },

                // General
                {
                        "fever", "cold", "cough", "flu",
                        "general physician", "physician"
                },

                // Mental health
                {
                        "mental", "stress", "anxiety",
                        "psychiatrist", "psychologist"
                },

                // Blood
                {
                        "blood", "anemia", "haematologist",
                        "hematologist"
                },

                // Allergy
                {
                        "allergy", "allergic",
                        "immunologist"
                },

                // Physiotherapy
                {
                        "physiotherapy", "muscle",
                        "physiotherapist"
                }
        };

        for (String[] group : groups) {

            boolean queryMatched = false;

            // Does query match a disease keyword?
            for (String keyword : group) {

                if (query.contains(keyword)) {
                    queryMatched = true;
                    break;
                }
            }

            if (!queryMatched) {
                continue;
            }

            // Does doctor specialization match this group?
            for (String keyword : group) {

                if (specialization.contains(keyword)) {
                    return true;
                }
            }
        }

        return false;
    }

    // LOAD DOCTORS FROM FIREBASE / VIEWMODEL
    private void initTopDoctors() {

        binding.ProgressBarDoctor.setVisibility(View.VISIBLE);

        viewModel.loadDoctors().observe(this, doctorsModels -> {

            if (doctorsModels != null) {

                allDoctors = new ArrayList<>(doctorsModels);

                filterDoctors(
                        binding.editTextText.getText().toString()
                );
            }

            binding.ProgressBarDoctor.setVisibility(View.GONE);
        });
    }

    // LOAD CATEGORIES
    private void initCategory() {

        binding.ProgressBarCat.setVisibility(View.VISIBLE);

        viewModel.loadCategory().observe(this, categoryModels -> {

            if (categoryModels != null) {

                binding.catView.setLayoutManager(
                        new LinearLayoutManager(
                                this,
                                LinearLayoutManager.HORIZONTAL,
                                false
                        )
                );

                binding.catView.setAdapter(
                        new CategoryAdapter(categoryModels)
                );
            }

            binding.ProgressBarCat.setVisibility(View.GONE);
        });
    }

    // LOGOUT
    private void logoutUser() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(
                MainActivity.this,
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}
