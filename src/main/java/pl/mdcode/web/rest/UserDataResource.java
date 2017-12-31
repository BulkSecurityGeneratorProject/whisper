package pl.mdcode.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.mdcode.domain.UserData;

import pl.mdcode.repository.UserDataRepository;
import pl.mdcode.web.rest.errors.BadRequestAlertException;
import pl.mdcode.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserData.
 */
@RestController
@RequestMapping("/api")
public class UserDataResource {

    private final Logger log = LoggerFactory.getLogger(UserDataResource.class);

    private static final String ENTITY_NAME = "userData";

    private final UserDataRepository userDataRepository;

    public UserDataResource(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    /**
     * POST  /user-data : Create a new userData.
     *
     * @param userData the userData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userData, or with status 400 (Bad Request) if the userData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-data")
    @Timed
    public ResponseEntity<UserData> createUserData(@Valid @RequestBody UserData userData) throws URISyntaxException {
        log.debug("REST request to save UserData : {}", userData);
        if (userData.getId() != null) {
            throw new BadRequestAlertException("A new userData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserData result = userDataRepository.save(userData);
        return ResponseEntity.created(new URI("/api/user-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-data : Updates an existing userData.
     *
     * @param userData the userData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userData,
     * or with status 400 (Bad Request) if the userData is not valid,
     * or with status 500 (Internal Server Error) if the userData couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-data")
    @Timed
    public ResponseEntity<UserData> updateUserData(@Valid @RequestBody UserData userData) throws URISyntaxException {
        log.debug("REST request to update UserData : {}", userData);
        if (userData.getId() == null) {
            return createUserData(userData);
        }
        UserData result = userDataRepository.save(userData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-data : get all the userData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userData in body
     */
    @GetMapping("/user-data")
    @Timed
    public List<UserData> getAllUserData() {
        log.debug("REST request to get all UserData");
        return userDataRepository.findAll();
        }

    /**
     * GET  /user-data/:id : get the "id" userData.
     *
     * @param id the id of the userData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userData, or with status 404 (Not Found)
     */
    @GetMapping("/user-data/{id}")
    @Timed
    public ResponseEntity<UserData> getUserData(@PathVariable Long id) {
        log.debug("REST request to get UserData : {}", id);
        UserData userData = userDataRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userData));
    }

    /**
     * DELETE  /user-data/:id : delete the "id" userData.
     *
     * @param id the id of the userData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserData(@PathVariable Long id) {
        log.debug("REST request to delete UserData : {}", id);
        userDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
