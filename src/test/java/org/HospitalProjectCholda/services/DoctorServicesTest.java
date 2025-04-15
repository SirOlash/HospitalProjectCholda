//package org.HospitalProjectCholda.services;
//
//import org.HospitalProjectCholda.data.models.Doctor;
//import org.HospitalProjectCholda.data.repositories.DoctorRepository;
//import org.HospitalProjectCholda.dtorequest.DoctorProfileDetailRequest;
//import org.HospitalProjectCholda.dtorequest.DoctorRegistrationRequest;
//import org.HospitalProjectCholda.dtorequest.DoctorResponseDTO;
//import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
//import org.HospitalProjectCholda.security.PasswordService;
//import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class DoctorServicesTest {
//
//    DoctorRegistrationRequest doctorRequest;
//
//    @Autowired
//    private PasswordService passwordService;
//    @Autowired
//    private DoctorServices doctorServices;
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    DoctorProfileDetailRequest doctorProfileDetail;
//
//    @BeforeEach
//    void setUp() {
//        doctorServices.deleteAll();
//
//
//        DoctorRegistrationRequest doctorRequest = new DoctorRegistrationRequest();
//
//
//
//        doctorRequest.setUserName("ben");
//        doctorRequest.setEmail("ben@gmail.com");
//        doctorRequest.setPassword("12345");
//        doctorServices.createNewDoctor(doctorRequest);
//
//
//
//
//    }
//    @AfterEach
//    void tearDown() {
//        doctorServices.deleteAll();
//    }
//    @Test
//    public void testThatYouCanRegisterDoctor() {
//
//        assertEquals(1,doctorServices.countAllDoctors());
//
//
//
//    }
//    @Test
//    public void testDoctorCanLogin_Successfully() {
//        DoctorResponseDTO doctor = doctorServices.doctorLogin("ben@gmail.com", "12345");
//
//        assertNotNull(doctor);
//        assertEquals("ben@gmail.com",doctor.getEmail());
//        assertEquals("ben",doctor.getUserName());
//
//    }
//    @Test
//    public void test_ThrowsDoctorNotFoundException() {
//
//        String email = "joe@gmail.com";
//        DoctorCollectionException wrongEmail = assertThrows(DoctorCollectionException.class, () -> doctorServices.doctorLogin(email, "123"));
//        assertEquals("Doctor with email: " + email + " not found", wrongEmail.getMessage());
//
//    }
//    @Test
//    public void test_ThrowsInvalidPasswordException() {
//        DoctorCollectionException wrongPassword = assertThrows(DoctorCollectionException.class, () -> doctorServices.doctorLogin("ben@gmail.com", "1235"));
//        assertEquals("Invalid email or password", wrongPassword.getMessage());
//    }
////    @Test
////    public void testDoctorRegistrationProfileCanBeUpdated() {
////        Doctor currentDoctor = doctorServices.doctorLogin("ben@gmail.com", "12345");
////
////        DoctorRegistrationRequest registrationRequest = new DoctorRegistrationRequest();
////        registrationRequest.setUserName("Stephen");
////        registrationRequest.setEmail("stephen@example.com");
////        registrationRequest.setPassword("2020");
////
////        doctorServices.updateDoctorProfile(currentDoctor.getId(), registrationRequest);
////
////        Doctor updatedDoctor = doctorRepository.findById(currentDoctor.getId())
////                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound(currentDoctor.getId())));
////        assertEquals("Stephen", updatedDoctor.getUserName());
////        assertEquals("stephen@example.com",  updatedDoctor.getEmail());
////        assertTrue(passwordService.matchesPassword("2020", updatedDoctor.getEncryptedPassword()));
////    }
//    @Test
//    public void testPatientProfileDetailCanBeUpdated(){
//        DoctorResponseDTO loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com", "12345");
//        doctorProfileDetail= new DoctorProfileDetailRequest();
//        doctorProfileDetail.setFirstName("Silas");
//        doctorProfileDetail.setLastName("Mina");
//        doctorProfileDetail.setAddress("Sabo-yaba");
//        doctorProfileDetail.setPhoneNumber("08164567890");
//
//        doctorServices.updateDoctorDetailedProfile(loggedInDoctor.getId(), doctorProfileDetail);
//        Doctor updatedDoctor = doctorRepository.findById(loggedInDoctor.getId())
//                .orElseThrow();
//
//        assertNotNull(updatedDoctor.getId());
//        assertEquals("Silas", updatedDoctor.getDoctorProfile().getFirstName());
//        assertEquals("Mina", updatedDoctor.getDoctorProfile().getLastName());
//        assertEquals("08164567890", updatedDoctor.getDoctorProfile().getPhoneNumber());
//        assertEquals("Sabo-yaba", updatedDoctor.getDoctorProfile().getAddress());
//        assertEquals("08164567890", updatedDoctor.getDoctorProfile().getPhoneNumber());
//
//    }
//
//}