package com.parkhomenko;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 *
 * @author dmytro
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void success_get_organization_application_josn_test() throws Exception {
        OrganizationDto organizationDto = fetchOrganization();
        Assert.assertEquals("AgileEngine", organizationDto.name);
    }
    
    @Test
    public void success_transaction_committe_application_json_test() throws Exception {
        OrganizationDto organizationBefore = fetchOrganization();
        
        final TransactionDto payload = new TransactionDto(null, null, true, BigDecimal.TEN, organizationBefore.id);

        mockMvc.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));
        
        OrganizationDto organizationAfter = fetchOrganization();
        
        Assert.assertEquals(organizationBefore.amount.subtract(BigDecimal.TEN), organizationAfter.amount);
    }
    
    /**
     * Fail: the amount of money on the account is not enough to process this transaction
     */
    @Test
    public void fail_huge_amount_transaction_committe_application_json_test() throws Exception {
        OrganizationDto organizationBefore = fetchOrganization();
        
        final TransactionDto payload = new TransactionDto(null, null, true, new BigDecimal(Integer.MAX_VALUE), organizationBefore.id);

        mockMvc.perform(post("/transactions")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));
        
        OrganizationDto organizationAfter = fetchOrganization();
        Assert.assertEquals(organizationBefore.amount, organizationAfter.amount);
    }

    private OrganizationDto fetchOrganization() throws IOException, Exception {
        MvcResult mvcResultGet = mockMvc.perform(get("/transactions/organization")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200)).andReturn();
        final OrganizationDto organizationDto = parceString(mvcResultGet, OrganizationDto.class, objectMapper);
        return organizationDto;
    }
    
    private static <T> T parceString(MvcResult result, Class<T> clazz, ObjectMapper mapper) throws IOException {
        String bodyAsString = result.getResponse().getContentAsString();
        return mapper.readValue(bodyAsString, clazz);
    }
}