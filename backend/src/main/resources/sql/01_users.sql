-- Insert into users
INSERT INTO users (
    id, name, email, contact, password, gender, date_of_birth, is_active, role
) VALUES (
             1,
             'Aonkon Saha',
             'aonkon@gmail.com',
             '01881264859',
             '$2a$10$ulXmahQgs0lCzyjGuMOmi.vQkl9VSL1QefY8aq1OUfXANAaj4PKZe',
             'Male',
             '1999-09-14',
             0,
             'DOCTOR'
         );

-- Insert into doctors (DoctorProfile)
INSERT INTO doctors (
    id, doctor_name, designation, license_number,
    years_of_experience, hospital_or_clinic_name,
    profile_picture_url, languages_spoken, rating,
    availability_status, user_id
) VALUES (
             1,
             'Aonkon Saha',
             'Cardiologist',
             'ABCDE$12FG',
             12,
             'SQUARE',
             NULL,
             'Bangla & English',
             NULL,
             NULL,
             1
         );

-- Insert degrees (Set<String> stored via ElementCollection)
INSERT INTO doctor_degrees (doctor_id, degree) VALUES
                                                   (1, 'MBBS(DU)'),
                                                   (1, 'DM(Neurology)');
