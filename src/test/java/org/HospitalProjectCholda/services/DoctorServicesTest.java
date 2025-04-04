package org.HospitalProjectCholda.services;

import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DoctorServicesTest {

    @Autowired
    private DoctorServices doctorServices;

    @BeforeEach
    void setUp() {
        doctorServices.deleteAll();

        Doctor signedUpDoctor = new Doctor();

        signedUpDoctor.setUserName("ben");
        signedUpDoctor.setEmail("ben@gmail.com");
        signedUpDoctor.setEncryptedPassword("1234");
        doctorServices.createNewDoctor(signedUpDoctor);

    }
    @AfterEach
    void tearDown() {
        doctorServices.deleteAll();
    }
    @Test
    public void testThatYouCanRegisterDoctor() {

        assertEquals(1,doctorServices.countAllDoctors());



    }
    @Test
    public void testDoctorCanLogin_Successfully() {
        Doctor doctor = doctorServices.doctorLogin("ben@gmail.com", "1234");

        assertNotNull(doctor);
        assertEquals("ben@gmail.com",doctor.getEmail());
        assertEquals("ben",doctor.getUserName());

    }
    @Test
    public void test_ThrowsDoctorNotFoundException() {

        String email = "joe@gmail.com";
        DoctorCollectionException wrongEmail = assertThrows(DoctorCollectionException.class, () -> doctorServices.doctorLogin(email, "123"));
        assertEquals("Doctor with email: " + email + " not found", wrongEmail.getMessage());

    }
    @Test
    public void test_ThrowsInvalidPasswordException() {

        String email = "ben@gmail.com";
        String password = "1235";
        DoctorCollectionException wrongPassword = assertThrows(DoctorCollectionException.class, () -> doctorServices.doctorLogin("ben@gmail.com", "1235"));
        assertEquals("Invalid email or password", wrongPassword.getMessage());
    }


}