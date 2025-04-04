package org.HospitalProjectCholda.services.appointmentservice;

import AppointmentStatus.AppointmentStatus;
import org.HospitalProjectCholda.data.models.Appointment;
import org.HospitalProjectCholda.data.models.Doctor;
import org.HospitalProjectCholda.data.models.Patient;
import org.HospitalProjectCholda.data.repositories.AppointmentRepository;
import org.HospitalProjectCholda.data.repositories.DoctorRepository;
import org.HospitalProjectCholda.dtorequest.AppointmentRequest;
import org.HospitalProjectCholda.exceptions.DoctorCollectionException;
import org.HospitalProjectCholda.services.doctorservice.DoctorServices;
import org.HospitalProjectCholda.services.patientservice.PatientServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
//

}
