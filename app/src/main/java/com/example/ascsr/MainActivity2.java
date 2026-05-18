package com.example.ascsr;



import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

    // ── Spinner ──
    private Spinner serviceSpinner;

    // ── Dynamic card + title ──
    private CardView dynamicCard;
    private TextView dynamicCardTitle;

    // ── Sections ──
    private LinearLayout hostelSection, wifiSection, labSection, eventSection;

    // ── Hostel ──
    private DatePicker hostelDatePicker;
    private TextInputEditText hostelDescription;

    // ── WiFi ──
    private Switch urgentSwitch;
    private TextView priorityLabel;
    private TextInputEditText wifiLocation;

    // ── Lab ──
    private RadioGroup equipmentGroup;

    // ── Event ──
    private TimePicker eventTimePicker;
    private TextView participantCount;
    private MaterialButton incrementBtn, decrementBtn;

    // ── Submit ──
    private MaterialButton submitBtn;

    // ── State ──
    private int participants = 1;

    // ── Service names matching spinner array ──
    private static final String[] SERVICE_LABELS = {
            "Select a service…",
            "Hostel Complaint",
            "WiFi Issue",
            "Lab Equipment Problem",
            "Event Registration"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bindViews();
        setupSpinner();
        setupWifiSwitch();
        setupParticipantCounter();
    }

    // ─────────────────────────────────────────────
    //  1. Bind all views
    // ─────────────────────────────────────────────
    private void bindViews() {
        serviceSpinner    = findViewById(R.id.serviceSpinner);
        dynamicCard       = findViewById(R.id.dynamicCard);
        dynamicCardTitle  = findViewById(R.id.dynamicCardTitle);

        hostelSection     = findViewById(R.id.hostelSection);
        wifiSection       = findViewById(R.id.wifiSection);
        labSection        = findViewById(R.id.labSection);
        eventSection      = findViewById(R.id.eventSection);

        hostelDatePicker  = findViewById(R.id.hostelDatePicker);
        hostelDescription = findViewById(R.id.hostelDescription);

        urgentSwitch      = findViewById(R.id.urgentSwitch);
        priorityLabel     = findViewById(R.id.priorityLabel);
        wifiLocation      = findViewById(R.id.wifiLocation);

        equipmentGroup    = findViewById(R.id.equipmentGroup);

        eventTimePicker   = findViewById(R.id.eventTimePicker);
        participantCount  = findViewById(R.id.participantCount);
        incrementBtn      = findViewById(R.id.incrementBtn);
        decrementBtn      = findViewById(R.id.decrementBtn);

        submitBtn         = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> showConfirmationDialog());
    }

    // ─────────────────────────────────────────────
    //  2. Spinner → show / hide sections
    // ─────────────────────────────────────────────
    private void setupSpinner() {
        String[] serviceTypes = {
                "Select a service…",
                "🏠  Hostel Complaint",
                "📶  WiFi Issue",
                "🔬  Lab Equipment Problem",
                "🎉  Event Registration"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                serviceTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hide all sections first
                hideAllSections();

                if (position == 0) {
                    // "Select a service…" — nothing to show
                    dynamicCard.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                    return;
                }

                // Show the dynamic card
                dynamicCard.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);

                switch (position) {
                    case 1: // Hostel Complaint
                        dynamicCardTitle.setText("  COMPLAINT DETAILS");
                        hostelSection.setVisibility(View.VISIBLE);
                        break;
                    case 2: // WiFi Issue
                        dynamicCardTitle.setText("  WIFI ISSUE DETAILS");
                        wifiSection.setVisibility(View.VISIBLE);
                        break;
                    case 3: // Lab Equipment
                        dynamicCardTitle.setText("  EQUIPMENT DETAILS");
                        labSection.setVisibility(View.VISIBLE);
                        break;
                    case 4: // Event Registration
                        dynamicCardTitle.setText("  EVENT DETAILS");
                        eventSection.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void hideAllSections() {
        hostelSection.setVisibility(View.VISIBLE);
        wifiSection.setVisibility(View.GONE);
        labSection.setVisibility(View.GONE);
        eventSection.setVisibility(View.GONE);
    }

    // ─────────────────────────────────────────────
    //  3. WiFi urgency switch label
    // ─────────────────────────────────────────────
    private void setupWifiSwitch() {
        urgentSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                priorityLabel.setText("⚡ Urgent — needs immediate attention");
                priorityLabel.setTextColor(0xFFFF6B6B);
            } else {
                priorityLabel.setText("Normal — will be handled shortly");
                priorityLabel.setTextColor(0xFF6B8AAF);
            }
        });
    }

    // ─────────────────────────────────────────────
    //  4. Participant counter (+ / −)
    // ─────────────────────────────────────────────
    private void setupParticipantCounter() {
        incrementBtn.setOnClickListener(v -> {
            participants++;
            participantCount.setText(String.valueOf(participants));
        });
        decrementBtn.setOnClickListener(v -> {
            if (participants > 1) {
                participants--;
                participantCount.setText(String.valueOf(participants));
            }
        });
    }

    // ─────────────────────────────────────────────
    //  5. Confirmation AlertDialog
    // ─────────────────────────────────────────────
    private void showConfirmationDialog() {
        int selectedPosition = serviceSpinner.getSelectedItemPosition();
        if (selectedPosition == 0) {
            Toast.makeText(this, "Please select a service type first.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the details summary
        String serviceName = SERVICE_LABELS[selectedPosition];
        StringBuilder details = new StringBuilder();
        details.append("📋  Service:  ").append(serviceName).append("\n\n");

        boolean valid = true;

        switch (selectedPosition) {
            case 1: { // Hostel
                int day   = hostelDatePicker.getDayOfMonth();
                int month = hostelDatePicker.getMonth() + 1;
                int year  = hostelDatePicker.getYear();
                String desc = hostelDescription.getText() != null
                        ? hostelDescription.getText().toString().trim() : "";

                if (desc.isEmpty()) {
                    hostelDescription.setError("Please describe the issue.");
                    valid = false;
                    break;
                }
                details.append("📅  Date:  ").append(day).append("/").append(month).append("/").append(year).append("\n\n");
                details.append("📝  Description:\n").append(desc);
                break;
            }
            case 2: { // WiFi
                String location = wifiLocation.getText() != null
                        ? wifiLocation.getText().toString().trim() : "";
                if (location.isEmpty()) {
                    wifiLocation.setError("Please enter a location.");
                    valid = false;
                    break;
                }
                String priority = urgentSwitch.isChecked() ? "⚡ Urgent" : "✅ Normal";
                details.append("🔴  Priority:  ").append(priority).append("\n\n");
                details.append("📍  Location:  ").append(location);
                break;
            }
            case 3: { // Lab
                int checkedId = equipmentGroup.getCheckedRadioButtonId();
                if (checkedId == -1) {
                    Toast.makeText(this, "Please select equipment type.", Toast.LENGTH_SHORT).show();
                    valid = false;
                    break;
                }
                RadioButton selected = findViewById(checkedId);
                details.append("🔧  Equipment:  ").append(selected.getText().toString().trim());
                break;
            }
            case 4: { // Event
                int hour   = eventTimePicker.getHour();
                int minute = eventTimePicker.getMinute();
                String am_pm = hour >= 12 ? "PM" : "AM";
                int displayHour = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
                String time = String.format("%d:%02d %s", displayHour, minute, am_pm);
                details.append("🕐  Time:  ").append(time).append("\n\n");
                details.append("👥  Participants:  ").append(participants);
                break;
            }
        }

        if (!valid) return;

        // Build and show the dialog
        new AlertDialog.Builder(this, R.style.DarkAlertDialogTheme)
                .setTitle("Confirm Submission")
                .setMessage(details.toString())
                .setPositiveButton("✔  Submit", (dialog, which) -> {
                    dialog.dismiss();
                    onSubmissionSuccess();
                })
                .setNegativeButton("✖  Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    // ─────────────────────────────────────────────
    //  6. Post-submission feedback
    // ─────────────────────────────────────────────
    private void onSubmissionSuccess() {
        Toast.makeText(this, "✅ Request submitted successfully!", Toast.LENGTH_LONG).show();
        // Reset the form
        serviceSpinner.setSelection(0);
        participants = 1;
        participantCount.setText("1");
        urgentSwitch.setChecked(false);
        equipmentGroup.clearCheck();
        hostelDescription.setText("");
        wifiLocation.setText("");
    }
}