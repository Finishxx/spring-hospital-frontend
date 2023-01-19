package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client;

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
public class PatientClient {

    private WebTarget patientEndpoint;
    private WebTarget singleTemplateEndpoint;
    private WebTarget activePatientEndpoint;

    public PatientClient(@Value("http://localhost:8080") String serverUrl) {
        Client client = ClientBuilder
                .newClient()
                .register(LoggingFeature.builder().level(Level.ALL).build());
        patientEndpoint = client.target(serverUrl + "/patients");
        singleTemplateEndpoint = patientEndpoint.path("/{id}");
    }

    public void setActivePatient(Long id) {activePatientEndpoint = singleTemplateEndpoint.resolveTemplate("id", id); }

    public PatientDto create(PatientDto patient) {
        return patientEndpoint
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(patient, MediaType.APPLICATION_JSON_TYPE), PatientDto.class);
    }

    public Optional<PatientDto> readOne() {
        Response response = activePatientEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() == 404)
            return Optional.empty();
        if (response.getStatus() == 200)
            return Optional.of(response.readEntity(PatientDto.class));
        throw new RuntimeException(response.getStatusInfo().getReasonPhrase());

    }

    public Collection<PatientDto> readAll() {
        PatientDto[] patientArray = patientEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get(PatientDto[].class);
        return Arrays.asList(patientArray);
    }

    public void updateOne(PatientDto patient) {
        Response response = activePatientEndpoint
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(patient, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }

    public void delete(Long id) {
        setActivePatient(id);
        activePatientEndpoint.request(MediaType.APPLICATION_JSON_TYPE).delete(PatientDto.class);
    }
}

