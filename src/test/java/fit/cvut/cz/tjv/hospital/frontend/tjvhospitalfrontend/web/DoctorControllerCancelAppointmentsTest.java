package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.TjvHospitalFrontendApplication;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.*;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.AppointmentService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.DoctorService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TjvHospitalFrontendApplication.class)
class DoctorControllerCancelAppointmentsTest {

    @Autowired
    private DoctorController doctorController;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private PatientService patientService;

    private DoctorDto doctor1;
    private Model model;
    InnerDoctorDto innerDoctor1;
    PatientDto patient1;
    InnerPatientDto innerPatient1;
    AppointmentDto appointment1;
    AppointmentDto appointment2;
    InnerAppointmentForDoctorDto innerAppointment1;
    InnerAppointmentForDoctorDto innerAppointment2;

    @BeforeEach
    void arrange() {
        model = new ConcurrentModel();
        doctor1 = new DoctorDto();
        doctor1.setId(1L);
        doctor1.setAppointments(new ArrayList<>());
        doctor1.setPatients(new ArrayList<>());
        doctor1.setEmailAddress("email");
        doctor1.setPhoneNumber("phone-number");
        doctor1.setName("Tomáš");

        innerDoctor1 = new InnerDoctorDto();
        innerDoctor1.setDoctor_name(doctor1.getName());
        innerDoctor1.setDoctor_id(doctor1.getId());

         patient1 = new PatientDto();
        patient1.setId(1L);
        patient1.setName("Jirka");

        innerPatient1 = new InnerPatientDto();
        innerPatient1.setPatient_name(patient1.getName());
        innerDoctor1.setDoctor_id(patient1.getId());

        appointment1 = new AppointmentDto();
        appointment1.setPatient(innerPatient1);
        appointment1.setDoctor(innerDoctor1);
        appointment1.setFrom(LocalDateTime.of(2000, 1, 1, 1, 1));
        appointment1.setTo(LocalDateTime.of(2000, 1, 1, 2, 2));
        appointment1.setId(1L);

        appointment2 = new AppointmentDto();
        appointment2.setPatient(innerPatient1);
        appointment2.setDoctor(innerDoctor1);
        appointment2.setFrom(LocalDateTime.of(2000, 1, 2, 2, 2));
        appointment2.setTo(LocalDateTime.of(2000, 1, 2, 3, 3));
        appointment2.setId(2L);

        innerAppointment1 = new InnerAppointmentForDoctorDto();
        innerAppointment1.setAppointment_id(appointment1.getId());

        innerAppointment2 = new InnerAppointmentForDoctorDto();
        innerAppointment2.setAppointment_id(appointment2.getId());


    }

    @Test
    void shouldBeVoid() {

        Mockito.when(doctorService.readOne()).thenReturn(Optional.of(doctor1));

        doctorController.cancelAppointments(doctor1.getId(), model);

        assertTrue(model.containsAttribute("doctor"));
        assertTrue(model.containsAttribute("dateAppointments"));
        assertEquals((DoctorDto) model.getAttribute("doctor"), doctor1);

        Map<LocalDate, Collection<AppointmentDto>> dateAppointmentsPairs = new HashMap<>();
        assertEquals(dateAppointmentsPairs, (Map<LocalDate, Collection<AppointmentDto>>) model.getAttribute("dateAppointments"));
    }

    @Test
    void shouldReturnOneDatePair() {

        List<InnerAppointmentForDoctorDto> innerAppointments = new ArrayList<>();
        innerAppointments.add(innerAppointment1);

        doctor1.setAppointments(innerAppointments);

        Mockito.when(doctorService.readOne()).thenReturn(Optional.of(doctor1));
        Mockito.when(appointmentService.readOne()).thenReturn(Optional.of(appointment1));

        doctorController.cancelAppointments(doctor1.getId(), model);

        assertTrue(model.containsAttribute("doctor"));
        assertTrue(model.containsAttribute("dateAppointments"));
        assertEquals((DoctorDto) model.getAttribute("doctor"), doctor1);

        Map<LocalDate, Collection<AppointmentDto>> dateAppointmentsPairs = new HashMap<>();
        dateAppointmentsPairs.put(appointment1.getFrom().toLocalDate(), List.of(appointment1));
        assertEquals(dateAppointmentsPairs, (Map<LocalDate, Collection<AppointmentDto>>) model.getAttribute("dateAppointments"));
        assertEquals(((Map<?, ?>) model.getAttribute("dateAppointments")).size(), 1);
    }

    @Test
    void shouldReturnTwoDatePairs() {

        List<InnerAppointmentForDoctorDto> innerAppointments = new ArrayList<>();
        innerAppointments.add(innerAppointment1);
        innerAppointments.add(innerAppointment2);

        doctor1.setAppointments(innerAppointments);

        Mockito.when(doctorService.readOne()).thenReturn(Optional.of(doctor1));
        Mockito.when(appointmentService.readOne()).
                thenReturn(Optional.of(appointment1)).
                thenReturn(Optional.of(appointment2));

        doctorController.cancelAppointments(doctor1.getId(), model);

        assertTrue(model.containsAttribute("doctor"));
        assertTrue(model.containsAttribute("dateAppointments"));
        assertEquals((DoctorDto) model.getAttribute("doctor"), doctor1);

        Map<LocalDate, Collection<AppointmentDto>> dateAppointmentsPairs = new HashMap<>();
        dateAppointmentsPairs.put(appointment1.getFrom().toLocalDate(), List.of(appointment1));
        dateAppointmentsPairs.put(appointment2.getFrom().toLocalDate(), List.of(appointment2));

        assertEquals(dateAppointmentsPairs, (Map<LocalDate, Collection<AppointmentDto>>) model.getAttribute("dateAppointments"));
        assertEquals(((Map<?, ?>) model.getAttribute("dateAppointments")).size(), 2);


    }
}