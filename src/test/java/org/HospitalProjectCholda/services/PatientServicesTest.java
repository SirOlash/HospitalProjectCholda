package org.HospitalProjectCholda.services;

import org.HospitalProjectCholda.data.models.Gender;
import org.HospitalProjectCholda.data.models.Patient;

import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.PatientProfileDetailRequest;
import org.HospitalProjectCholda.dtorequest.PatientRegistrationRequest;
import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PatientServicesTest {
    @Autowired
    private PatientServices patientServices;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private PatientRepository patientRepository;


    @BeforeEach
    void setUp() {
        patientServices.deleteAll();

        PatientRegistrationRequest registrationRequest = new PatientRegistrationRequest();

        registrationRequest.setUserName("John");
        registrationRequest.setEmail("john@example.com");

        registrationRequest.setPassword("password");


        patientServices.createNewPatient(registrationRequest);


    }
    @AfterEach
    void tearDown() {
        patientServices.deleteAll();
    }
    @Test
    public void test_Patient_Can_register() {
        assertEquals(1, patientServices.countAllPatients());

    }
    @Test
    public void test_login_In_Patient_Successfully() {
        Patient loggedInPatient = patientServices.patientLogin("john@example.com", "password");
        assertNotNull(loggedInPatient);
        assertEquals("john@example.com", loggedInPatient.getEmail());
        assertEquals("John", loggedInPatient.getUserName());

    }
    @Test
    public void test_login_Throws_PatientWithEmailNotFoundException() {
        String email = "sam@example.com";
        PatientCollectionException wrongEmail = assertThrows(PatientCollectionException.class, () -> patientServices.patientLogin(email, "password"));
        assertEquals("Patient with email: " + email + " not found", wrongEmail.getMessage());
    }
    @Test
    public void test_login_Throws_InvalidPasswordOrEmailException() {
        String email = "john@example.com";
        String password = "wordpass";
        PatientCollectionException wrongPassword = assertThrows(PatientCollectionException.class, () -> patientServices.patientLogin(email, password));
        assertEquals("Invalid email or password", wrongPassword.getMessage());

    }

    @Test
    public void testPatientRegistrationProfileCanBeUpdated() {
        Patient currentPatient = patientServices.patientLogin("john@example.com", "password");

        PatientRegistrationRequest registrationRequest = new PatientRegistrationRequest();
        registrationRequest.setUserName("Stephen");
        registrationRequest.setEmail("stephen@example.com");
        registrationRequest.setPassword("2020");

        patientServices.updatePatientProfile(currentPatient.getId(), registrationRequest);

        Patient updatedPatient = patientRepository.findById(currentPatient.getId()).get();
        assertEquals("Stephen", updatedPatient.getUserName());
        assertEquals("stephen@example.com",  updatedPatient.getEmail());
        assertTrue(passwordService.matchesPassword("2020", updatedPatient.getEncryptedPassword()));
    }
    @Test
    public void testPatientProfileDetailCanBeUpdated(){
        Patient loggedInPatient = patientServices.patientLogin("john@example.com", "password");
        PatientProfileDetailRequest profile = new PatientProfileDetailRequest();
        profile.setFirstName("Silas");
        profile.setLastName("Mina");
        profile.setGender(String.valueOf(Gender.MALE));
        profile.setDateOfBirth(String.valueOf(LocalDate.of(1990, 8, 13)));
        profile.setAddress("Sabo-yaba");
        profile.setPhoneNumber("08164567890");

        patientServices.updatePatientDetailedProfile(loggedInPatient.getId(), profile);
        Patient updatedPatient = patientRepository.findById(loggedInPatient.getId())
                .orElseThrow();

        assertNotNull(updatedPatient.getId());
        assertEquals("Silas", updatedPatient.getPatientProfile().getFirstName());
        assertEquals("Mina", updatedPatient.getPatientProfile().getLastName());
        assertEquals("08164567890", updatedPatient.getPatientProfile().getPhoneNumber());
        assertEquals(Gender.MALE, updatedPatient.getPatientProfile().getGender());
        assertEquals(LocalDate.of(1990, 8, 13), updatedPatient.getPatientProfile().getDateOfBirth());
        assertEquals("Sabo-yaba", updatedPatient.getPatientProfile().getAddress());
        assertEquals("08164567890", updatedPatient.getPatientProfile().getPhoneNumber());

    }

}