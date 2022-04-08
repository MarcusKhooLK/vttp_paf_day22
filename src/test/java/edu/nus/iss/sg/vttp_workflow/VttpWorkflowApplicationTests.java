package edu.nus.iss.sg.vttp_workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.nus.iss.sg.vttp_workflow.services.GifServices;

@SpringBootTest
@AutoConfigureMockMvc
class VttpWorkflowApplicationTests {

    @Autowired
    private GifServices gifSvc;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryGiphyAndShouldReturnFixedWidth() {
        List<String> images = gifSvc.searchGifImages("pokemon");
        assertTrue(images.size() == 10);

        images = gifSvc.searchGifImages("pokemon", 10);
        assertTrue(images.size() == 10);

        images = gifSvc.searchGifImages("pokemon", "PG-13");
        assertTrue(images.size() == 10);
    }

    // @Test
    // void postQueryGiphy() throws Exception {
    //     RequestBuilder req = MockMvcRequestBuilders.post("/postGif")
    //                                                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
    //                                                 .content(buildUrlEncodedFormEntity("phrase", "pokemon",
    //                                                 "limit", "10",
    //                                                 "rating", "PG_13"));

    //     MvcResult result = mockMvc.perform(req).andReturn();
    //     int status = result.getResponse().getStatus();

    //     String payload = result.getResponse().getContentAsString();

    //     assertEquals(200, status);
    //     assertTrue(payload.indexOf("Results") > 0);
    //     assertTrue(!payload.isEmpty());
    // }

    private String buildUrlEncodedFormEntity(String... params) {
        if( (params.length % 2) > 0 ) {
           throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<params.length; i+=2) {
            if( i > 0 ) {
                result.append('&');
            }
            try {
                result.
                append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
                append('=').
                append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8.name()));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
     }

}
