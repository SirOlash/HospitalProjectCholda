package org.HospitalProjectCholda.services;

import org.HospitalProjectCholda.data.models.Patient;

import org.HospitalProjectCholda.exceptions.PatientCollectionException;
import org.HospitalProjectCholda.security.PasswordService;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PatientServicesTest {
    @Autowired
    private PatientServices patientServices;
    @Autowired
    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        patientServices.deleteAll();

        Patient signedUpPatient = new Patient();

        signedUpPatient.setUserName("John");
        signedUpPatient.setEmail("john@example.com");
        signedUpPatient.setEncryptedPassword("password");
        patientServices.createNewPatient(signedUpPatient);


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


}