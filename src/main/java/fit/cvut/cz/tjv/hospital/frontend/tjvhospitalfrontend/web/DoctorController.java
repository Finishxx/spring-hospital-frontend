package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import ch.qos.logback.core.joran.sanity.Pair;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client.AppointmentClient;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.*;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.AppointmentService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.DoctorService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private DoctorService doctorService;
    private PatientService patientService;
    private AppointmentService appointmentService;


    public DoctorController(DoctorService doctorService,
                            PatientService patientService,
                            AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String doctor(Model model) {
        model.addAttribute("allDoctors", doctorService.readAll());
        return "doctors";
    }

    @GetMapping("/edit")
    public String editDoctor(@RequestParam Long id, Model model) {
        doctorService.setActiveDoctor(id);
        model.addAttribute("doctor", doctorService.readOne().orElseThrow());
        return "doctorsEdit";
    }

    @PostMapping("/edit")
    public String submitEditDoctor(@ModelAttribute DoctorDto doctor, Model model) {
        doctorService.setActiveDoctor(doctor.getId());
        DoctorDto originalDoctor = doctorService.readOne().orElseThrow(); // sad :c
        // doctor.setAppointments(originalDoctor.getAppointments());
        doctor.setPatients(originalDoctor.getPatients());
        try {
            doctorService.update(doctor);
        } catch (BadRequestException e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }
        model.addAttribute("doctor", doctor);
        return "doctorsEdit";
    }

    @GetMapping("/addPatient")
    public String addPatient(@RequestParam Long id, Model model) {
        doctorService.setActiveDoctor(id);
        DoctorDto doctor = doctorService.readOne().orElseThrow();
        Collection<PatientDto> patients = patientService.readAll();
        Collection<Long> illegalPatients = doctor.getPatients().stream().
                map(InnerPatientDto::getPatient_id).
                toList();
        Collection<PatientDto> filteredPatients = patients.stream().
                filter(patientDto -> !illegalPatients.contains(patientDto.getId())).
                toList();

        model.addAttribute("patients", filteredPatients);
        model.addAttribute("doctor", doctor);
        return "doctorsAddPatient";
    }

    @PostMapping("/addPatient")
    public String addedPatient(@RequestParam Long id, @RequestParam Long added, Model model) {
        doctorService.setActiveDoctor(id);
        DoctorDto doctor = doctorService.readOne().orElseThrow();
        patientService.setActivePatient(added);
        PatientDto patient = patientService.readOne().orElseThrow();
        InnerPatientDto innerPatientDto = new InnerPatientDto();
        innerPatientDto.setPatient_id(added);
        innerPatientDto.setPatient_name(patient.getName());
        doctor.getPatients().add(innerPatientDto);
        doctorService.update(doctor);

        return addPatient(id, model); //Will this work? xDD - it does!
    }

    @GetMapping("/removePatient")
    public String removePatient(@RequestParam Long id, Model model) {
        doctorService.setActiveDoctor(id);
        DoctorDto doctor = doctorService.readOne().orElseThrow();
        Collection<PatientDto> patients = patientService.readAll();
        Collection<Long> validPatients = doctor.getPatients().stream().
                map(InnerPatientDto::getPatient_id).
                toList();
        Collection<PatientDto> filteredPatients = patients.stream().
                filter(patientDto -> validPatients.contains(patientDto.getId()))
                .toList();

        model.addAttribute("doctor", doctor);
        model.addAttribute("patients", filteredPatients);

        return "doctorsRemovePatient";
    }

    @PostMapping("/removePatient")
    public String removedPatient(@RequestParam Long id, @RequestParam Long removed, Model model) {
        doctorService.setActiveDoctor(id);
        DoctorDto doctor = doctorService.readOne().orElseThrow();
        patientService.setActivePatient(removed);
        doctor.getPatients().removeIf(innerPatientDto -> innerPatientDto.getPatient_id() == removed);
        doctorService.update(doctor);

        return removePatient(id, model);
    }

    @GetMapping("/create")
    public String createPatient(Model model) {
        model.addAttribute("doctor", new DoctorDto());
        return "doctorsCreate";
    }

    @PostMapping("/create")
    public String submitCreatePatient(@ModelAttribute DoctorDto doctor, Model model) {
        doctorService.create(doctor);
        return createPatient(model);
    }

    @GetMapping("/delete")
    public String deleteDoctor(@RequestParam Long id, Model model) {
        doctorService.delete(id);;
        return doctor(model);
    }

    @GetMapping("/cancelAppointments")
    public String cancelAppointments(@RequestParam Long id, Model model) {

        ArrayList<AppointmentDto> appointments = new ArrayList<>();
        doctorService.setActiveDoctor(id);
        DoctorDto doctor = doctorService.readOne().orElseThrow();

        for (InnerAppointmentForDoctorDto innerAppointment : doctor.getAppointments()) {
            appointmentService.setActiveAppointment(innerAppointment.getAppointment_id());
            appointments.add(appointmentService.readOne().orElseThrow());
        }
        Map<LocalDate, Collection<AppointmentDto>> dateAppointmentPairs = new HashMap<>();

        for (AppointmentDto appointment : appointments)
            System.out.println("All dates: " + appointment.getFrom().toLocalDate());

        appointments.stream().forEach(appointment -> {
            var collection = dateAppointmentPairs.get(appointment.getFrom().toLocalDate());
            System.out.println("Looking for date: " + appointment.getFrom().toLocalDate());
            System.out.println("It is: " + collection == null);
            if ( collection == null )
                dateAppointmentPairs.put(appointment.getFrom().toLocalDate(), new ArrayList<>(List.of(appointment)));
            if (collection != null)
                collection.add(appointment);
        });

        model.addAttribute("dateAppointments", dateAppointmentPairs);
        model.addAttribute("doctor", doctor);
        return "doctorsCancelAppointment";
    }

    @PostMapping("/cancelAppointments")
    public String submitCancelAppointments(@RequestParam Long id, @RequestParam Long cancelled, Model model) {
        doctorService.setActiveDoctor(id);
        DoctorDto doctor = doctorService.readOne().orElseThrow();

        appointmentService.setActiveAppointment(cancelled);
        AppointmentDto cancelledAppointment = appointmentService.readOne().orElseThrow();

        LocalDate cancelDate = cancelledAppointment.getFrom().toLocalDate();

        List<Long> toCancelIds = doctor.getAppointments().stream().
                filter(appointment -> appointment.getTime_from().toLocalDate().equals(cancelDate)).
                map(InnerAppointmentForDoctorDto::getAppointment_id).
                toList();
        for (Long appointmentId : toCancelIds)
            appointmentService.delete(appointmentId);


        return cancelAppointments(doctor.getId(), model);
    }

}
