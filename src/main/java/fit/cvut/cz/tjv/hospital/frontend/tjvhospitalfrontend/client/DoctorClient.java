package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.DoctorDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;

public class DoctorClient {

    private WebTarget doctorEndpoint;
    private WebTarget singleTemplateEndpoint;
    private WebTarget activeDoctorEndpoint;

    public DoctorClient(@Value("http://localhost:8080") String serverUrl) {
        Client client = ClientBuilder
                .newClient()
                .register(LoggingFeature.builder().level(Level.ALL).build());
        doctorEndpoint = client.target(serverUrl + "/doctor");
        singleTemplateEndpoint = doctorEndpoint.path("/{id}");
    }


    public void setActiveDoctor(Long id) {activeDoctorEndpoint = singleTemplateEndpoint.resolveTemplate("id", id); }

    public DoctorDto create(DoctorDto doctor) {
        return doctorEndpoint
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(doctor, MediaType.APPLICATION_JSON_TYPE), DoctorDto.class);
    }

    public Optional<DoctorDto> readOne() {
        Response response = activeDoctorEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() == 404)
            return Optional.empty();
        if (response.getStatus() == 200)
            return Optional.of(response.readEntity(DoctorDto.class));
        throw new RuntimeException(response.getStatusInfo().getReasonPhrase());

    }

    public Collection<DoctorDto> readAll() {
        DoctorDto[] doctorArray = doctorEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get(DoctorDto[].class);
        return Arrays.asList(doctorArray);
    }

    public void updateOne(DoctorDto doctor) {
        Response response = activeDoctorEndpoint
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(doctor, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }
}
