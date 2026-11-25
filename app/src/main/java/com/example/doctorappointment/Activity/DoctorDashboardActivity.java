package com.example.doctorappointment.Activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DoctorDashboardActivity extends AppCompatActivity {
    private LinearLayout listContainer; private FirebaseAuth auth;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); auth=FirebaseAuth.getInstance();
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,20,20,20); root.setBackgroundColor(0xFFF5F7FB);
        TextView title=new TextView(this); title.setText("My Patient Appointments"); title.setTextSize(23); title.setTextColor(0xFF1565C0); title.setGravity(Gravity.CENTER); root.addView(title);
        Button refresh=new Button(this); refresh.setText("Refresh Appointments"); root.addView(refresh);
        ScrollView scroll=new ScrollView(this); listContainer=new LinearLayout(this); listContainer.setOrientation(LinearLayout.VERTICAL); scroll.addView(listContainer); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        Button logout=new Button(this); logout.setText("Logout"); root.addView(logout); setContentView(root);
        refresh.setOnClickListener(v -> loadAppointments()); logout.setOnClickListener(v->{auth.signOut(); finish();}); loadAppointments();
    }
    private void loadAppointments(){
        listContainer.removeAllViews(); if(auth.getCurrentUser()==null){finish();return;}
        FirebaseFirestore.getInstance().collection("Appointments").whereEqualTo("doctorUid",auth.getCurrentUser().getUid()).get().addOnSuccessListener(snap->{
            if(snap.isEmpty()){ addText("No appointments found for this doctor account. Check that the doctor UID is linked in Realtime Database."); return;}
            for(QueryDocumentSnapshot doc:snap){ LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(20,16,20,16); card.setBackgroundColor(0xFFFFFFFF); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,16); card.setLayoutParams(lp);
                card.addView(text("Patient: "+safe(doc.getString("patientName")))); card.addView(text("Doctor: "+safe(doc.getString("doctorName")))); card.addView(text("Specialization: "+safe(doc.getString("specialization")))); card.addView(text("Date: "+safe(doc.getString("appointmentDate")))); card.addView(text("Time: "+safe(doc.getString("appointmentTime")))); card.addView(text("Status: "+safe(doc.getString("status")))); listContainer.addView(card); }
        }).addOnFailureListener(e->Toast.makeText(this,"Could not load appointments: "+e.getMessage(),Toast.LENGTH_LONG).show());
    }
    private void addText(String value){TextView t=text(value); t.setPadding(12,20,12,20); listContainer.addView(t);}
    private TextView text(String value){TextView t=new TextView(this);t.setText(value);t.setTextSize(16);t.setTextColor(0xFF222222);t.setPadding(0,4,0,4);return t;}
    private String safe(String s){return s==null||s.trim().isEmpty()?"N/A":s;}
}
