package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDTO;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class BeerControllerIT {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testPatchBeerLongName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Nameasdfasdfasfsfasfasfasfasdfsfsaffsgdhdfgdfgsgafafgdhdgasfgagadhadsgadgadsfg");

        MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPatchBeerNotFound() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        String beerName = "PatchedBeer";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.patchBeerById(UUID.randomUUID(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    @Rollback
    @Transactional
    void testPatchBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        String beerName = "PatchedBeer";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.patchBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(beerRepository.findById(beer.getId()).get().getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testDeleteBeerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }

    @Test
    @Rollback
    @Transactional
    void testDeleteBeer() {
        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void testUpdateBeerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateBeerById(UUID.randomUUID(),BeerDTO.builder().build());
        });
    }

    @Test
    @Rollback
    @Transactional
    void testUpdateExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        String beerName = "UpdatedBeer";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer beer1 = beerRepository.findById(beer.getId()).get();
        assertThat(beer1.getBeerName()).isEqualTo(beerName);

    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewBeer(){
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity responseEntity = beerController.saveNewBeer(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void testGetBeerByIdNotFound() {
        assertThrows(NotFoundException.class,
                () -> beerController.getBeerById(UUID.randomUUID()));

    }

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerController.getBeerById(beer.getId());

        assertThat(beerDTO).isNotNull();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> beerDTOList = beerController.listBeers();

        assertThat(beerDTOList.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> beerDTOList = beerController.listBeers();

        assertThat(beerDTOList.size()).isEqualTo(0);
    }
}
