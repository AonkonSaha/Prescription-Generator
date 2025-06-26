-- Insert into users
INSERT IGNORE INTO users (
    id, name, email, contact, password, gender,
    date_of_birth, is_active, created_date, last_updated_date
) VALUES (
             1,
             'Aonkon Saha',
             'aonkon@gmail.com',
             '01881264859',
             '$2a$10$ulXmahQgs0lCzyjGuMOmi.vQkl9VSL1QefY8aq1OUfXANAaj4PKZe',  -- bcrypt password
             'Male',
             '1999-09-14',
             false,
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP
         );

-- Insert into doctors (DoctorProfile)
INSERT IGNORE INTO doctors (
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
             TRUE,
             1
         );


-- Insert degrees (Set<String> stored via ElementCollection)
INSERT IGNORE INTO doctor_degrees (doctor_id, degree) VALUES
                                                   (1, 'MBBS(DU)'),
                                                   (1, 'DM(Neurology)');
-- Insert roles
INSERT IGNORE INTO roles (id, role_name, role_description, role_status) VALUES
    (1, 'DOCTOR', 'Doctor role', 'ACTIVE');
INSERT IGNORE INTO roles (id, role_name, role_description, role_status) VALUES
    (2, 'ADMIN', 'Admin role', 'ACTIVE');

INSERT IGNORE INTO user_roles (role_id, user_id) VALUES
    (1, 1);
INSERT IGNORE INTO user_roles (role_id, user_id) VALUES
    (2, 1);
