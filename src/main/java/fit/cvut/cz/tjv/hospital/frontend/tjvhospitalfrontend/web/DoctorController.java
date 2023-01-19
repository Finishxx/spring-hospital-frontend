package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.DoctorDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.InnerPatientDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.PatientDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.DoctorService;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private DoctorService doctorService;
    private PatientService patientService;

    public DoctorController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
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
        doctorService.delete(id);
        return doctor(model);
    }

}
