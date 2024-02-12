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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ovs_aas.RyuController.Utils.HttpDeleteWithBody;

public class Controller extends AbstractController {

    public String makeRequestWithoutSerialize(String URL) throws IOException, InterruptedException {
        CloseableHttpResponse resp = this.apacheClient.execute(new HttpGet(URL));
        return EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
    }
    
    public <T> T makeRequestWithSerialization(String URL, TypeReference<T> typeRef) {
        try {
            CloseableHttpResponse resp = this.apacheClient.execute(new HttpGet(URL));
            return objMap.readValue(EntityUtils.toString(resp.getEntity()), typeRef);
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer setControllerRole(String URL, int switchNumber, String role) {
        ObjectNode jsonBody = objMap.createObjectNode();
        jsonBody.put("dpid", switchNumber);
        jsonBody.put("role", role);

        return this.postRequest(URL, jsonBody);
    }

    public Integer putRequest(String url) {
        HttpPut httpPut = new HttpPut(url);
        int statusCode = 0;
        String body = "";
        try {
            HttpResponse response = apacheClient.execute(httpPut);
            statusCode = response.getStatusLine().getStatusCode();
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (statusCode != HTTP_OK)
            try {
                throw new BadHttpRequest(new Exception(String.format("Result: %d %s", statusCode, body)));
            } catch (BadHttpRequest e) {
                e.printStackTrace();
            }
        return statusCode;
    }

    public Integer postFirewallRules(String URL, String src, String dst, String actions) {
        ObjectNode jsonBody = objMap.createObjectNode();

        if (!src.equals("10.0.0.0")) {
            jsonBody.put("nw_src", src);
        }

        if (!dst.equals("10.0.0.0")) {
            jsonBody.put("nw_dst", dst);
        }
        
        jsonBody.put("nw_proto", "ICMP");
        jsonBody.put("actions", actions);

        return this.postRequest(URL, jsonBody);
    }

    public Integer deleteFirewallRule(String URL, int rule_id) {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(URL);

        ObjectNode body = objMap.createObjectNode();
        body.put("rule_id", rule_id);
        
        int statusCode = 0;
        String responseBody = "";

        try {
            httpDelete.setEntity(new StringEntity(body.toString()));  
            HttpResponse response = apacheClient.execute(httpDelete);
            statusCode = response.getStatusLine().getStatusCode();
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (statusCode != HTTP_OK)
            try {
                throw new BadHttpRequest(new Exception(String.format("Result: %d %s", statusCode, responseBody)));
            } catch (BadHttpRequest e) {
                e.printStackTrace();
            }
        return statusCode;
    }

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