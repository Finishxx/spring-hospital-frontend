package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.DoctorDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.PatientDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.DoctorService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/doctor_patient")
public class DoctorPatientController {

    private DoctorService doctorService;
    private PatientService patientService;

    public DoctorPatientController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }


    @GetMapping("/edit")
    public String edit(@RequestParam Optional<Long> id_doctor,
                             @RequestParam Optional<Long> id_patient,
                             Model model) {
        if (id_doctor.isPresent()) {
            model.addAttribute("id_doctor", id_doctor.get());
        }

        if (id_patient.isPresent()) {
            model.addAttribute("id_patient", id_patient.get());
        }

        model.addAttribute("patients", patientService.readAll());
        model.addAttribute("doctors", doctorService.readAll());

        return "doctorPatientEdit";
    }
}
/*

@Controller
@RequestMapping("/doctors")
public class DoctorController {


    @GetMapping
    public String doctor(Model model) {
        model.addAttribute("allDoctors", doctorService.readAll());
        var doctors = doctorService.readAll();
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
        doctor.setAppointments(originalDoctor.getAppointments());
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
}
*/
