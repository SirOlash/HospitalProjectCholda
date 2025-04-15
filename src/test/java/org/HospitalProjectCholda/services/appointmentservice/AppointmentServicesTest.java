//package org.HospitalProjectCholda.services.appointmentservice;
//
//import org.HospitalProjectCholda.data.models.*;
//import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
//import org.HospitalProjectCholda.data.repositories.DoctorRepository;
//import org.HospitalProjectCholda.data.repositories.PatientRepository;
//import org.HospitalProjectCholda.dtorequest.*;
//import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
//import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
//import org.HospitalProjectCholda.exceptions.PatientCollectionException;
//import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
//import org.HospitalProjectCholda.services.patientservice.PatientServices;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//class AppointmentServicesTest {
//
//    private Doctor signedUpDoctor;
//
//    @Autowired
//    private AppointmentServices appointmentServices;
//
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private PatientServices patientServices;
//
//
//    PatientRegistrationRequest patientRegistrationRequest;
//
//    @Autowired
//    private DoctorServices doctorServices;
//
//    AppointmentRequest appointmentRequest;
//    @Autowired
//    private DoctorRepository doctorRepository;
//    @Autowired
//    private PatientRepository patientRepository;
//
//    @BeforeEach
//    public void setUp(){
//        appointmentServices.deleteAll();
//        patientServices.deleteAll();
//        doctorServices.deleteAll();
//
//
//        patientRegistrationRequest = new PatientRegistrationRequest();
//
//
//
//
//        patientRegistrationRequest.setUserName("John");
//        patientRegistrationRequest.setEmail("john@example.com");
//        patientRegistrationRequest.setPassword("password");
//        patientServices.createNewPatient(patientRegistrationRequest);
//        Patient loggedInPatient = patientServices.patientLogin("john@example.com", "password");
//
//        if (loggedInPatient.getPatientProfile() != null) {
//            PatientProfile patientProfile = new PatientProfile();
//            patientProfile.setFirstName("Johnson");
//            patientProfile.setLastName("Friday");
//            loggedInPatient.setPatientProfile(patientProfile);
//            patientRepository.save(loggedInPatient);
//        }
//
//        if (loggedInPatient.getMedicalHistory() != null) {
//            loggedInPatient.setMedicalHistory(new ArrayList<>());
//        }
//        patientRepository.save(loggedInPatient);
//
//
//
//
//
//        DoctorRegistrationRequest doctorRequest = new DoctorRegistrationRequest();
//        doctorRequest.setUserName("ben");
//        doctorRequest.setEmail("ben@gmail.com");
//        doctorRequest.setPassword("password");
//        doctorRequest.setAvailable(true);
//        DoctorResponseDTO createdDoctor = doctorServices.createNewDoctor(doctorRequest);
//
//        DoctorProfile doctorProfile = new DoctorProfile();
//        doctorProfile.setFirstName("Francis");
//        doctorProfile.setLastName("McLean");
//        createdDoctor.setDoctorProfile(doctorProfile);
//        doctorRepository.save(createdDoctor);
//
//
//
//        this.appointmentRequest = new AppointmentRequest();
//        appointmentRequest.setPatient(loggedInPatient);
//        appointmentRequest.setDoctorEmail("ben@gmail.com");
//        appointmentRequest.setDescription("description");
//        appointmentRequest.setAppointmentTime(LocalDateTime.of(2020, 6, 1, 3, 20));
//
//
//    }
//    @Test
//    public void test_Patient_Can_Book_Appointment(){
//        AppointmentResponseDTO bookedAppointment = appointmentServices.createAppointment(appointmentRequest);
//        System.out.println(bookedAppointment);
//        assertNotNull(bookedAppointment);
//        assertEquals("ben@gmail.com", bookedAppointment.getDoctor().getEmail());
//        assertEquals("John", bookedAppointment.getPatient().getUserName());
//        assertEquals("john@example.com", bookedAppointment.getPatient().getEmail());
//        assertEquals("description", bookedAppointment.getDescription());
//        assertEquals(LocalDateTime.of(2020, 6, 1, 3, 20), bookedAppointment.getAppointmentTime());
//        assertFalse(bookedAppointment.getDoctor().isAvailable());
//
//    }
//    @Test
//    public void test_Throws_DoctorAlreadyBookedException(){
//
//        PatientRegistrationRequest patient2 = new PatientRegistrationRequest();
//        patient2.setUserName("Mark");
//        patient2.setEmail("mark@example.com");
//        patient2.setPassword("password");
//        patientServices.createNewPatient(patient2);
//
//        DoctorRegistrationRequest doctorRequest = new DoctorRegistrationRequest();
//        doctorRequest.setUserName("Stephen");
//        doctorRequest.setEmail("stephen@example.com");
//        doctorRequest.setPassword("password");
//        doctorRequest.setAvailable(false);
//        doctorServices.createNewDoctor(doctorRequest);
//
//
//        Patient patient2LoggedIn = patientServices.patientLogin("mark@example.com", "password");
//        this.appointmentRequest = new AppointmentRequest();
//        appointmentRequest.setPatient(patient2LoggedIn);
//        appointmentRequest.setDoctorEmail("stephen@example.com");
//        appointmentRequest.setDescription("Malaria parasite");
//        assertThrows(DoctorCollectionException.class, () ->patientServices.bookAppointment(appointmentRequest));
//    }
//
//    @Test
//    public void testThatPatientCanViewAvailableDoctors() {
//
//        DoctorRegistrationRequest doctorRequest2 = new DoctorRegistrationRequest();
//        doctorRequest2.setUserName("ben2");
//        doctorRequest2.setEmail("ben2@gmail.com");
//        doctorRequest2.setPassword("password");
//        doctorRequest2.setAvailable(true);
//        doctorServices.createNewDoctor(doctorRequest2);
//
//        DoctorRegistrationRequest doctorRequest3 = new DoctorRegistrationRequest();
//        doctorRequest3.setUserName("ben3");
//        doctorRequest3.setEmail("ben3@gmail.com");
//        doctorRequest3.setPassword("password");
//        doctorRequest3.setAvailable(true);
//        doctorServices.createNewDoctor(doctorRequest3);
//
//
//
//        PatientRegistrationRequest signedUpPatient2 = new PatientRegistrationRequest();
//        signedUpPatient2.setUserName("John");
//        signedUpPatient2.setEmail("joh@example.com");
//        signedUpPatient2.setPassword("password");
//        patientServices.createNewPatient(signedUpPatient2);
//        Patient loggedInPatient2 = patientServices.patientLogin("joh@example.com", "password");
//
//        AppointmentRequest appointmentRequest2 = new AppointmentRequest();
//        appointmentRequest2.setPatient(loggedInPatient2);
//        appointmentRequest2.setDoctorEmail("ben3@gmail.com");
//        appointmentRequest2.setDescription("description");
//
//        assertEquals(3,doctorServices.viewAvailableDoctors().size());
//    }
//
//    @Test
//    public void testThatPatientCanOnlyBookAvailableDoctors(){
//        AppointmentResponseDTO bookedAppointment = appointmentServices.createAppointment(appointmentRequest);
//        assertNotNull(bookedAppointment);
//        assertEquals("John", bookedAppointment.getPatient().getUserName());
//        assertEquals("john@example.com", bookedAppointment.getPatient().getEmail());
//        assertEquals("description", bookedAppointment.getDescription());
//        assertEquals("ben@gmail.com", bookedAppointment.getDoctor().getEmail());
//        assertFalse(bookedAppointment.getDoctor().isAvailable());
//
//
//        PatientRegistrationRequest signedUpPatient2 = new PatientRegistrationRequest();
//        signedUpPatient2.setUserName("John");
//        signedUpPatient2.setEmail("joh@example.com");
//        signedUpPatient2.setPassword("password");
//        patientServices.createNewPatient(signedUpPatient2);
//        Patient loggedInPatient2 = patientServices.patientLogin("joh@example.com", "password");
//
//
//        AppointmentRequest appointmentRequest2 = new AppointmentRequest();
//        appointmentRequest2.setPatient(loggedInPatient2);
//        appointmentRequest2.setDoctorEmail("ben@gmail.com");
//        appointmentRequest2.setDescription("description");
//
//
//        assertThrows(DoctorCollectionException.class, () -> appointmentServices.createAppointment(appointmentRequest2));
//
//    }
//
//    @Test
//    public void testThatDoctorCanCheckAppointment(){
//
//        AppointmentResponseDTO bookedAppointment = appointmentServices.createAppointment(appointmentRequest);
//
//        DoctorResponseDTO loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com", "password");
//
//        AppointmentResponseDTO appointment = doctorServices.viewAppointment("ben@gmail.com");
//        assertEquals(bookedAppointment.getDoctor().getUserName(), loggedInDoctor.getUserName());
//        assertEquals(bookedAppointment.getDoctor().getEmail(), loggedInDoctor.getEmail());
//        assertEquals(appointmentRequest.getDescription(), appointment.getDescription());
//
//    }
//
//    @Test
//    public void testThrowsNoBookedAppointmentException(){
//        DoctorResponseDTO loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com","password");
//        assertThrows(AppointmentCollectionException.class, () -> doctorServices.viewAppointment("ben@gmail.com"));
//    }
//
//    @Test
//    public void testThatDoctorCanAcceptAppointment(){
//
//        AppointmentResponseDTO bookedAppointment = appointmentServices.createAppointment(appointmentRequest);
//        DoctorResponseDTO loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com",  "password");
//        doctorServices.viewAppointment("ben@gmail.com");
//        assertEquals(bookedAppointment.getDoctor().getUserName(), loggedInDoctor.getUserName());
//        assertEquals(bookedAppointment.getDoctor().getEmail(), loggedInDoctor.getEmail());
//        assertEquals("John", bookedAppointment.getPatient().getUserName());
//        assertFalse(loggedInDoctor.isAvailable());
//
//        String treatment = "Take 500mg of paracetamol everyday";
//
//        Appointment acceptedAppointment = doctorServices.acceptAppointment("ben@gmail.com", treatment);
//
//        Patient appointedPatient = patientRepository.findByEmail("john@example.com")
//                .orElseThrow(() -> new PatientCollectionException(PatientCollectionException.PatientWithEmailNotFound("john@example.com")));
//
//        assertEquals(1, appointedPatient.getMedicalHistory().size());
//        assertEquals(treatment, appointedPatient.getMedicalHistory().get(0).getTreatment());
//        assertEquals("description", bookedAppointment.getDescription());
//
//    }
//    @Test
//    public void testThatDoctorAvailabilityStatusGetsUpdatedAfterAppointment(){
//        this.testThatDoctorCanAcceptAppointment();
//        Doctor appointedDoctor = doctorRepository.findByEmail("ben@gmail.com")
//                .orElseThrow(() -> new DoctorCollectionException(DoctorCollectionException.DoctorNotFound("ben@gmail.com")));
//        assertTrue(appointedDoctor.isAvailable());
//
//
//    }
//
//}
