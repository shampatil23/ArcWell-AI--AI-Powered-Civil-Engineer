package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;



        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class RegEng extends AppCompatActivity {
    private EditText etOptionTitle, etOptionDescription,etskill,etphone,etexp,etemail;
    private Button btnAddOption;
    private DatabaseReference engineersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regeng);
        etskill=findViewById(R.id.skille);
        etphone=findViewById(R.id.number);
        etexp=findViewById(R.id.expe);
        etemail=findViewById(R.id.emaile);

        etOptionTitle = findViewById(R.id.etOptionTitlee);
        etOptionDescription = findViewById(R.id.etOptionDescriptione);
        btnAddOption = findViewById(R.id.btnAddOptione);


        // Initialize Firebase reference
        engineersRef = FirebaseDatabase.getInstance().getReference("reg");

        btnAddOption.setOnClickListener(v -> {
            String title = etOptionTitle.getText().toString().trim();
            String description = etOptionDescription.getText().toString().trim();
            String E=etemail.getText().toString().trim();
            String EE=etexp.getText().toString().trim();
            String s=etskill.getText().toString().trim();
            String p=etphone.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()|| p.isEmpty()|| s.isEmpty()|| EE.isEmpty()|| E.isEmpty()) {
                Toast.makeText(RegEng.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use a default image for admin‑added option (ensure drawable exists)
            int defaultImage = R.drawable.enggg;
            // For admin-added options, you may leave email, skills, experience empty or set default values.
            EngineerModel newOption = new EngineerModel(defaultImage, title, description, E, s, p);

            String key = engineersRef.push().getKey();
            if (key != null) {
                engineersRef.child(key).setValue(newOption)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegEng.this, "Registration success ! \n We will notify on your Mail", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegEng.this, "Error adding option", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}


