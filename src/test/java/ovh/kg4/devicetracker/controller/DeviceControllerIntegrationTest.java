package ovh.kg4.devicetracker.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ovh.kg4.devicetracker.DeviceTrackerApplication;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = DeviceTrackerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeviceControllerIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser(username = "test-user", password = "test-password")
  void getAll() throws Exception {

    mvc.perform(get("/devices")
        .with(httpBasic("test-user", "test-password"))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(9)))
//      .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId()), Long.class))
//      .andExpect(jsonPath("$[0].booked_at", Matchers.is(booking.getBookedAt()), Long.class))
//      .andExpect(jsonPath("$[0].booked_by", Matchers.is(booking.getBookedBy().getName())))
//      .andExpect(jsonPath("$[0].userId", Matchers.is(booking.getBookedBy().getId()), Long.class))
//      .andExpect(jsonPath("$[0].deviceName", Matchers.is(booking.getDevice().getModelName())))
//      .andExpect(jsonPath("$[0].deviceId", Matchers.is(booking.getDevice().getId()), Long.class))
    ;
  }

  @Test
  @WithMockUser(username = "test-user", password = "test-password")
  void getAllWithBookingInfo() throws Exception {

    mvc.perform(get("/devices/status")
        .with(httpBasic("test-user", "test-password"))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(9)))
    ;
  }

  @Test
  @WithMockUser(username = "test-user", password = "test-password")
  void getById() throws Exception {

    mvc.perform(get("/devices/1")
        .with(httpBasic("test-user", "test-password"))
        .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", Matchers.is(1L), Long.class))
    ;
  }
}