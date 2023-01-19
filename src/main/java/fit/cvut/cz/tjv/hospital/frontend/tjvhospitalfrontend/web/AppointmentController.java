package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.*;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.AppointmentService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.DoctorService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;
    private PatientService patientService;
    private DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String appointment(Model model) {
        model.addAttribute("allAppointments", appointmentService.readAll());
        return "appointments";
    }

    @GetMapping("/edit")
    public String editAppointment(@RequestParam Long id, Model model) {
        appointmentService.setActiveAppointment(id);
        model.addAttribute("appointment", appointmentService.readOne().orElseThrow() );
        return "appointmentsEdit";
    }

    @PostMapping("/edit")
    public String submitEditAppointment(@ModelAttribute AppointmentDto appointment, Model model) {
        appointmentService.setActiveAppointment(appointment.getId());
        AppointmentDto originalAppointment = appointmentService.readOne().orElseThrow();
        originalAppointment.setFrom(appointment.getFrom());
        originalAppointment.setTo(appointment.getTo());

        try {
            appointmentService.update(originalAppointment);
        } catch(BadRequestException e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }
        model.addAttribute("appointment", appointment);
        return editAppointment(appointment.getId(), model);
    }

    @GetMapping("/create")
    public String createAppointment(Model model) {
        model.addAttribute("allDoctors", doctorService.readAll());
        model.addAttribute("allPatients", patientService.readAll());
        model.addAttribute("appointment", new AppointmentDto());

        return "appointmentsCreate";
    }

    @PostMapping("/create")
    public String submitCreateAppointment(@ModelAttribute AppointmentDto appointment, Model model) {

        if (appointment.getFrom().isAfter(appointment.getTo())) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", "Invalid time: from is after to!");
            return createAppointment(model);
        }
        doctorService.setActiveDoctor(appointment.getDoctor().getDoctor_id());
        DoctorDto fullDoctor = doctorService.readOne().orElseThrow();

        patientService.setActivePatient(appointment.getPatient().getPatient_id());
        PatientDto fullPatient = patientService.readOne().orElseThrow();

        // the names are not needed, but seems right
        appointment.getDoctor().setDoctor_name(fullDoctor.getName());
        appointment.getPatient().setPatient_name(fullPatient.getName());

        appointmentService.create(appointment);

        return createAppointment(model);
    }

    @GetMapping("delete")
    public String deleteAppointment(@RequestParam Long id, Model model) {
        appointmentService.delete(id);
        return appointment(model);
    }

}
