package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService) {
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

        return "doctorPatientEdit";
    }
}
