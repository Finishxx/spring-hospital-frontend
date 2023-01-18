package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client.AppointmentClient;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.AppointmentDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AppointmentService {
    private AppointmentClient appointmentClient;
    private boolean isAppointmentActive = false;

    public AppointmentService(AppointmentClient appointmentClient) { this.appointmentClient = appointmentClient; }

    void create(AppointmentDto patient) { appointmentClient.create(patient); }

    public void setActiveAppointment(Long id) {
        appointmentClient.setActiveAppointment(id);
        isAppointmentActive = true;
    }

    public boolean idPatientActive() { return isAppointmentActive; }

    public Optional<AppointmentDto> readOne() { return appointmentClient.readOne(); }

    public Collection<AppointmentDto> readAll() { return appointmentClient.readAll(); }

    public void update(AppointmentDto appointment) { appointmentClient.updateOne(appointment); }

}
