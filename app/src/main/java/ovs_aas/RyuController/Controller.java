// Copyright 2023 riccardo.bacca@studio.unibo.it
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package ovs_aas.RyuController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ovs_aas.RyuController.Utils.HttpDeleteWithBody;

public class Controller extends AbstractController {

    /**
     * @param URL
     * @return String response of Get request without serialization required
     * @throws IOException
     * @throws InterruptedException
     */
    public String makeRequestWithoutSerialize(String URL) throws IOException, InterruptedException {
        CloseableHttpResponse resp = this.apacheClient.execute(new HttpGet(URL));
        return EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
    }
    
    /**
     * @param <T> type on which to serialize the response
     * @param URL
     * @param typeRef
     * @return Parsed result string for type <T>
     */
    public <T> T makeRequestWithSerialization(String URL, TypeReference<T> typeRef) {
        try {
            CloseableHttpResponse resp = this.apacheClient.execute(new HttpGet(URL));
            return objMap.readValue(EntityUtils.toString(resp.getEntity()), typeRef);
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param URL
     * @param switchNumber 1 o 2
     * @param role for the controller (Master or Slave)
     * @return statusCode from Post request
     * @throws HttpResponseException 
     */
    public Integer setControllerRole(String URL, int switchNumber, String role) throws HttpResponseException {
        ObjectNode jsonBody = objMap.createObjectNode();
        jsonBody.put("dpid", switchNumber);
        jsonBody.put("role", role);

        return this.postRequest(URL, jsonBody);
    }

    /**
     * @param url
     * @return status code
     * @throws HttpResponseException 
     */
    public Integer putRequest(String url) throws HttpResponseException {
        HttpPut httpPut = new HttpPut(url);
        int statusCode = 0;
        String statusMessage = "";
        try {
            HttpResponse response = apacheClient.execute(httpPut);
            statusCode = response.getStatusLine().getStatusCode();
            statusMessage = response.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (statusCode != HTTP_OK)
            throw new HttpResponseException(statusCode, statusMessage);

        return statusCode;
    }

    /**
     * @param URL on which to make request (Ryu Controller IP)
     * @param src Source IP for specific firewall Rule
     * @param dst Destination IP for firewall rule
     * @param actions Deny or Allow
     * @return code 200 if all OK
     * @return others if request did not succeeded
     * @throws HttpResponseException 
     */
    public Integer postFirewallRules(String URL, String src, String dst, String actions, int priority) throws HttpResponseException {
        ObjectNode jsonBody = objMap.createObjectNode();

        if (!src.isBlank()) {
            jsonBody.put("nw_src", src);
        }

        if (!dst.isBlank()) {
            jsonBody.put("nw_dst", dst);
        }
        
        jsonBody.put("nw_proto", "ICMP");
        jsonBody.put("actions", actions);
        jsonBody.put("priority", priority);

        return this.postRequest(URL, jsonBody);
    }

    /**
     * @param URL
     * @param rule_id to be deleted
     * @return code 200 if all OK
     * @return others if request did not succeeded
     * @throws HttpResponseException 
     */
    public Integer deleteFirewallRule(String URL, int rule_id) throws HttpResponseException {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(URL);

        ObjectNode body = objMap.createObjectNode();
        body.put("rule_id", rule_id);
        
        int statusCode = 0;
        String statusMessage = "";
        CloseableHttpResponse response = null;

        try {
            httpDelete.setEntity(new StringEntity(body.toString()));  
            response = apacheClient.execute(httpDelete);
            statusCode = response.getStatusLine().getStatusCode();
            statusMessage = response.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {}
            }
        }

        if (statusCode != HTTP_OK)
            throw new HttpResponseException(statusCode, statusMessage);
            
        return statusCode;
    }

    /**
     * @param URL for Server on which to Poll for a positive response
     * @return true if Server on provided URL reply with StatusCode 200
     * @return false otherwise
     */
    public Boolean isServerAvailable(String URL) {
        HttpGet getRequest = new HttpGet(URL);
        int statusCode = 0;
        CloseableHttpResponse response = null;

        try {
            response = apacheClient.execute(getRequest);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (IOException e) {} 
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {}
            }
        }

        return statusCode == HTTP_OK;
    }
}