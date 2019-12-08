package kkalinowski.springframework.kkpetclinic.controller;

import kkalinowski.springframework.kkpetclinic.model.Owner;
import kkalinowski.springframework.kkpetclinic.model.Pet;
import kkalinowski.springframework.kkpetclinic.model.PetType;
import kkalinowski.springframework.kkpetclinic.service.PetService;
import kkalinowski.springframework.kkpetclinic.service.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    private static final String PETS_CREATE_OR_UPDATE_VISIT_FORM = "pets/createOrUpdateVisitForm";
    private static final String REDIRECT_OWNERS_1 = "redirect:/owners/{ownerId}";
    private static final String YET_ANOTHER_VISIT_DESCRIPTION = "yet another visit";

    @Mock
    PetService petService;

    @Mock
    VisitService visitService;

    VisitController visitController;

    private MockMvc mockMvc;

    private final UriTemplate visitsUriTemplate = new UriTemplate("/owners/{ownerId}/pets/{petId}/visits/new");
    private final Map<String, String> uriVariables = new HashMap<>();
    private URI visitsUri;

    @BeforeEach
    void setUp() {
        visitController = new VisitController(visitService, petService);

        Long petId = 1L;
        Long ownerId = 1L;
        when(petService.findById(anyLong()))
                .thenReturn(
                        Pet.builder().id(petId)
                                .birthDate(LocalDate.of(2015,01,01))
                                .name("Bingo")
                                .visits(new HashSet<>())
                                .owner(Owner.builder()
                                        .id(ownerId)
                                        .firstName("Krzysztof")
                                        .lastName("Kalinowski")
                                        .build())
                                .petType(PetType.builder().name("Dog").build())
                                .build()
                );

        uriVariables.clear();
        uriVariables.put("ownerId", ownerId.toString());
        uriVariables.put("petId", petId.toString());
        visitsUri = visitsUriTemplate.expand(uriVariables);

        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
    }

    @Test
    void initNewVisitFormTest() throws Exception {
        mockMvc.perform(get(visitsUri))
                .andExpect(status().isOk())
                .andExpect(view().name(PETS_CREATE_OR_UPDATE_VISIT_FORM))
        ;
    }


    @Test
    void processNewVisitFormTest() throws Exception {
        mockMvc.perform(post(visitsUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("date","2019-12-08")
                .param("description", YET_ANOTHER_VISIT_DESCRIPTION))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_OWNERS_1))
                .andExpect(model().attributeExists("visit"))
        ;
    }
}