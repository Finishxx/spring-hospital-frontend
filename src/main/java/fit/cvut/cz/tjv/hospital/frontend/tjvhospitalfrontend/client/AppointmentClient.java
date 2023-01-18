package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.AppointmentDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.PatientDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;

@Component
public class AppointmentClient {

    private WebTarget appointmentEndpoint;
    private WebTarget singleTemplateEndpoint;
    private WebTarget activeAppointmentEndpoint;

    public AppointmentClient(@Value("http://localhost:8080") String serverUrl) {
        Client client = ClientBuilder
                .newClient()
                .register(LoggingFeature.builder().level(Level.ALL).build());
        appointmentEndpoint = client.target(serverUrl + "/appointments");
        singleTemplateEndpoint = appointmentEndpoint.path("/{id}");
    }

    public void setActiveAppointment(Long id) {
        activeAppointmentEndpoint = singleTemplateEndpoint.resolveTemplate("id", id); }

    public AppointmentDto create(AppointmentDto appointment) {
        return appointmentEndpoint
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(appointment, MediaType.APPLICATION_JSON_TYPE), AppointmentDto.class);
    }

    public Optional<AppointmentDto> readOne() {
        Response response = activeAppointmentEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() == 404)
            return Optional.empty();
        if (response.getStatus() == 200)
            return Optional.of(response.readEntity(AppointmentDto.class));
        throw new RuntimeException(response.getStatusInfo().getReasonPhrase());

    }

    public Collection<AppointmentDto> readAll() {
        AppointmentDto[] appointmentArray = appointmentEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get(AppointmentDto[].class);
        return Arrays.asList(appointmentArray);
    }

    public void updateOne(AppointmentDto appointment) {
        Response response = activeAppointmentEndpoint
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(appointment, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }
}

