-- --------------------- Report 1 and Prescription 1 ---------------------
INSERT IGNORE INTO reports (id, report_day, doctor_id) VALUES
    (1, '2025-06-01', 1);

INSERT IGNORE INTO prescriptions (
    id, prescription_date, patient_name, patient_age, patient_gender,
    next_visit_date, prescription_month, prescription_year,
    doctor_id, report_id
) VALUES (
             1, '2025-06-01', 'Aonkon Saha', 24, 'Male',
             '2025-06-02', 6, 2025,
             1, 1
         );

INSERT IGNORE INTO prescription_diagnosis (prescription_id, diagnosis) VALUES
                                                                    (1, 'Blood Test'),
                                                                    (1, 'Urine Test');

INSERT IGNORE INTO prescription_medicines (prescription_id, medicine) VALUES
                                                                   (1, 'Napa'),
                                                                   (1, 'Seclo 20');

-- --------------------- Report 2 and Prescription 2 ---------------------
INSERT IGNORE INTO reports (id, report_day, doctor_id) VALUES
    (2, '2025-06-02', 1);

INSERT IGNORE INTO prescriptions (
    id, prescription_date, patient_name, patient_age, patient_gender,
    next_visit_date, prescription_month, prescription_year,
    doctor_id, report_id
) VALUES (
             2, '2025-06-02', 'Sohel Ahmed', 24, 'Male',
             '2025-06-03', 6, 2025,
             1, 2
         );

INSERT IGNORE INTO prescription_diagnosis (prescription_id, diagnosis) VALUES
                                                                    (2, 'X-Ray'),
                                                                    (2, 'CBC');

INSERT IGNORE INTO prescription_medicines (prescription_id, medicine) VALUES
                                                                   (2, 'Napa Extra'),
                                                                   (2, 'Seclo 40');

-- --------------------- Report 3 and Prescription 3 ---------------------
INSERT IGNORE INTO reports (id, report_day, doctor_id) VALUES
    (3, '2025-06-03', 1);

INSERT IGNORE INTO prescriptions (
    id, prescription_date, patient_name, patient_age, patient_gender,
    next_visit_date, prescription_month, prescription_year,
    doctor_id, report_id
) VALUES (
             3, '2025-06-03', 'Aonkon Saha', 24, 'Male',
             '2025-06-04', 6, 2025,
             1, 3
         );

INSERT IGNORE INTO prescription_diagnosis (prescription_id, diagnosis) VALUES
                                                                    (3, 'Blood Test'),
                                                                    (3, 'Urine Test');

INSERT IGNORE INTO prescription_medicines (prescription_id, medicine) VALUES
                                                                   (3, 'Napa'),
                                                                   (3, 'Seclo 20');
