package org.HospitalProjectCholda.services.appointmentservice;

import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.MedicalHistory;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.data.repositories.PatientRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.exceptions.AppointmentCollectionException;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AppointmentServicesTest {

    private Doctor signedUpDoctor;

    @Autowired
    private AppointmentServices appointmentServices;


    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientServices patientServices;

    @Autowired
    private DoctorServices doctorServices;

    AppointmentRequest appointmentRequest;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    public void setUp(){
        appointmentServices.deleteAll();
        patientServices.deleteAll();
        doctorServices.deleteAll();

        Patient signedUpPatient = new Patient();
        signedUpPatient.setUserName("John");
        signedUpPatient.setEmail("john@example.com");
        signedUpPatient.setEncryptedPassword("password");
        patientServices.createNewPatient(signedUpPatient);
        Patient loggedInPatient = patientServices.patientLogin("john@example.com", "password");


        signedUpDoctor = new Doctor();
        signedUpDoctor.setUserName("ben");
        signedUpDoctor.setEmail("ben@gmail.com");
        signedUpDoctor.setEncryptedPassword("1234");
        signedUpDoctor.setAvailable(true);
        doctorServices.createNewDoctor(signedUpDoctor);


        this.appointmentRequest = new AppointmentRequest();
        appointmentRequest.setPatient(loggedInPatient);
        appointmentRequest.setDoctorEmail("ben@gmail.com");
        appointmentRequest.setDescription("description");


    }
    @Test
    public void test_Patient_Can_Book_Appointment(){
        Appointment bookedAppointment = appointmentServices.createAppointment(appointmentRequest);
        System.out.println(bookedAppointment);
        assertNotNull(bookedAppointment);
        assertEquals("John", bookedAppointment.getPatient().getUserName());
        assertEquals("john@example.com", bookedAppointment.getPatient().getEmail());
        assertEquals("description", bookedAppointment.getDescription());
        assertFalse(bookedAppointment.getDoctor().isAvailable());

    }
    @Test
    public void test_Throws_DoctorAlreadyBookedException(){
        Patient patient2 = new Patient();
        patient2.setUserName("Mark");
        patient2.setEmail("mark@example.com");
        patient2.setEncryptedPassword("0000");
        patientServices.createNewPatient(patient2);

        Doctor doctor = new Doctor();
        doctor.setUserName("Stephen");
        doctor.setEmail("stephen@example.com");
        doctor.setEncryptedPassword("1234");
        doctor.setAvailable(false);
        doctorRepository.save(doctor);


        Patient patient2LoggedIn = patientServices.patientLogin("mark@example.com", "0000");
        this.appointmentRequest = new AppointmentRequest();
        appointmentRequest.setPatient(patient2LoggedIn);
        appointmentRequest.setDoctorEmail("stephen@example.com");
        appointmentRequest.setDescription("Malaria parasite");
        assertThrows(DoctorCollectionException.class, () ->patientServices.bookAppointment(appointmentRequest));
    }
    @Test
    public void test_Throws_DoctorIs_Set_Available_When_No_Appointment() throws DoctorCollectionException {
        assertTrue(doctorRepository.findById(signedUpDoctor.getId())
                .orElseThrow().isAvailable());
        doctorServices.updateDoctorAvailability(signedUpDoctor.getId(), true);
        Doctor availableDoctor = doctorRepository.findById(signedUpDoctor.getId())
                .orElseThrow();

        assertTrue(availableDoctor.isAvailable());

    }
    @Test
    public void test_Doctor_Can_Accept_Appointment_Only_When_Booked_And_Is_Available(){
        Appointment appointment = appointmentServices.createAppointment(appointmentRequest);

        Doctor foundDoctor = doctorRepository.findByEmail("ben@gmail.com")
                .orElseThrow();
        assertTrue(foundDoctor.isAvailable());

        boolean acceptAppointment = doctorServices.isAppointmentAccepted(foundDoctor, appointment.getId());
        assertTrue(acceptAppointment);

        Doctor refreshedDoctor = doctorRepository.findByEmail("ben@gmail.com")
                .orElseThrow();

        assertFalse(refreshedDoctor.isAvailable());
        assertTrue(refreshedDoctor.isHasAcceptedAppointment());

    }
    @Test
    public void test_Doctor_Cannot_Accept_When_Unavailble(){

        Appointment appointment = appointmentServices.createAppointment(appointmentRequest);

        Doctor foundDoctor = doctorRepository.findByEmail("ben@gmail.com")
                .orElseThrow();
        assertTrue(foundDoctor.isAvailable());

        boolean acceptAppointment = doctorServices.isAppointmentAccepted(foundDoctor, appointment.getId());
        assertTrue(acceptAppointment);

        Doctor refreshedDoctor = doctorRepository.findByEmail("ben@gmail.com")
                .orElseThrow();

        assertFalse(refreshedDoctor.isAvailable());
        assertTrue(refreshedDoctor.isHasAcceptedAppointment());

         boolean acceptAppointment2 = doctorServices.isAppointmentAccepted(refreshedDoctor, appointment.getId());
         assertFalse(acceptAppointment2);
    }
    @Test
    public void test_Doctor_can_Update_Patients_MedicalRecord(){
        Appointment appointment = appointmentServices.createAppointment(appointmentRequest);

        Doctor foundDoctor = doctorRepository.findByEmail("ben@gmail.com")
                .orElseThrow();
        assertTrue(foundDoctor.isAvailable());

        boolean acceptAppointment = doctorServices.isAppointmentAccepted(foundDoctor, appointment.getId());
        assertTrue(acceptAppointment);

        Doctor refreshedDoctor = doctorRepository.findByEmail("ben@gmail.com")
                .orElseThrow();

        assertFalse(refreshedDoctor.isAvailable());
        assertTrue(refreshedDoctor.isHasAcceptedAppointment());

        MedicalHistory medicalHistory = new MedicalHistory(
                LocalDateTime.now(),
                "Malaria parasite",
                "Luma tern 500mg prescribed to patient"
        );
        doctorServices.fillMedicalReport(refreshedDoctor.getId(), medicalHistory);
        Patient patientTreated = patientRepository.findById(appointment.getPatient().getId())
                .orElseThrow();

        assertFalse(patientTreated.getMedicalHistory().isEmpty());

        MedicalHistory updatedPatientRecord = patientTreated.getMedicalHistory().get(0);

        assertEquals("Malaria parasite", updatedPatientRecord.getDescription());
        assertEquals("Luma tern 500mg prescribed to patient", updatedPatientRecord.getTreatment());


    }

    @Test
    public void testThatPatientCanViewAvailableDoctors() {
//        appointmentServices.createAppointment(appointmentRequest);
//        assertTrue(signedUpDoctor.isAvailable());
        Doctor signedUpDoctor2 = new Doctor();
        signedUpDoctor2.setUserName("ben2");
        signedUpDoctor2.setEmail("ben2@gmail.com");
        signedUpDoctor2.setEncryptedPassword("1234");
        signedUpDoctor2.setAvailable(true);
        doctorServices.createNewDoctor(signedUpDoctor2);

        Doctor signedUpDoctor3 = new Doctor();
        signedUpDoctor3.setUserName("ben3");
        signedUpDoctor3.setEmail("ben3@gmail.com");
        signedUpDoctor3.setEncryptedPassword("1234");
        signedUpDoctor3.setAvailable(true);
        doctorServices.createNewDoctor(signedUpDoctor3);



        Patient signedUpPatient2 = new Patient();
        signedUpPatient2.setUserName("John");
        signedUpPatient2.setEmail("joh@example.com");
        signedUpPatient2.setEncryptedPassword("password");
        patientServices.createNewPatient(signedUpPatient2);
        Patient loggedInPatient2 = patientServices.patientLogin("joh@example.com", "password");

        AppointmentRequest appointmentRequest2 = new AppointmentRequest();
        appointmentRequest2.setPatient(loggedInPatient2);
        appointmentRequest2.setDoctorEmail("ben3@gmail.com");
        appointmentRequest2.setDescription("description");

//        Appointment appointment2 = appointmentServices.createAppointment(appointmentRequest2);
//        System.out.println(appointment2.getDoctor().getUserName());
//        System.out.println(appointment2.getDoctor().isAvailable());




        assertEquals(3,doctorServices.viewAvailableDoctors().size());
//        System.out.println(doctorServices.viewAvailableDoctors());
        //loggedInPatient
    }

    @Test
    public void testThatPatientCanOnlyBookAvailableDoctors(){
        Appointment bookedAppointment = appointmentServices.createAppointment(appointmentRequest);
        assertNotNull(bookedAppointment);
        assertEquals("John", bookedAppointment.getPatient().getUserName());
        assertEquals("john@example.com", bookedAppointment.getPatient().getEmail());
        assertEquals("description", bookedAppointment.getDescription());
        assertFalse(bookedAppointment.getDoctor().isAvailable());

        Patient signedUpPatient2 = new Patient();
        signedUpPatient2.setUserName("John");
        signedUpPatient2.setEmail("joh@example.com");
        signedUpPatient2.setEncryptedPassword("password");
        patientServices.createNewPatient(signedUpPatient2);
        Patient loggedInPatient2 = patientServices.patientLogin("joh@example.com", "password");

        AppointmentRequest appointmentRequest2 = new AppointmentRequest();
        appointmentRequest2.setPatient(loggedInPatient2);
        appointmentRequest2.setDoctorEmail("ben@gmail.com");
        appointmentRequest2.setDescription("description");

        assertThrows(DoctorCollectionException.class, () -> appointmentServices.createAppointment(appointmentRequest2));

    }

    @Test
    public void testThatDoctorCanCheckAppointment(){
        appointmentServices.createAppointment(appointmentRequest);

        Doctor loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com","1234");
        Appointment currentAppointment = doctorServices.viewAppointment(loggedInDoctor);
        assertEquals("John", currentAppointment.getPatient().getUserName());
        assertEquals("john@example.com", currentAppointment.getPatient().getEmail());

    }

    @Test
    public void testThrowsNoBookedAppointmentException(){
        Doctor loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com","1234");
        assertThrows(AppointmentCollectionException.class, () -> doctorServices.viewAppointment(loggedInDoctor));
    }

    @Test
    public void testThatDoctorCanAcceptAppointment(){
        appointmentServices.createAppointment(appointmentRequest);

        Doctor loggedInDoctor = doctorServices.doctorLogin("ben@gmail.com","1234");
        Appointment currentAppointment = doctorServices.viewAppointment(loggedInDoctor);
        assertEquals("John", currentAppointment.getPatient().getUserName());

        Appointment appointment = doctorServices.acceptAppointment(loggedInDoctor, "Take paracetamol");
        MedicalHistory medicalHistory = appointment.getPatient().getMedicalHistory().get(0);

        assertEquals("Take paracetamol", medicalHistory.getTreatment());
        assertEquals("description", medicalHistory.getDescription());

    }

}
