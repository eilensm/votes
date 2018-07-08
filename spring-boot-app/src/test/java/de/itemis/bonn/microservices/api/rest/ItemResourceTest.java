package de.itemis.bonn.microservices.api.rest;

import de.itemis.bonn.rating.VotingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
public class ItemResourceTest {

  private static final String ENDPOINT = "/items";
  private static final String DESCRIPTION = "blub";

  private MockMvc mockMvc;

  private VotingService votingService;

  @Before
  public void setUp() {
    votingService = mock(VotingService.class);
    mockMvc = standaloneSetup(new ItemResource(votingService)).build();
  }

  @Test
  public void shouldcreateItem() throws Exception {
    mockMvc
        .perform(
            post(ENDPOINT)
                .content("{\"description\": \"blub\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(votingService).createItem(DESCRIPTION);
  }
}
