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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
public abstract class AbstractController {
    protected static final int HTTP_OK = 200;
    CloseableHttpClient apacheClient;
    ObjectMapper objMap;

    public AbstractController() {
        this.apacheClient = HttpClients.createDefault();
        this.objMap = new ObjectMapper();
    }

    /**
     * @param URL
     * @param body
     * @return Post request Status Code
     * @throws HttpResponseException 
     */
    public Integer postRequest(String URL, ObjectNode body) throws HttpResponseException {
        int statusCode = 0;
        String statusMessage = "";
        HttpPost httpPost = new HttpPost(URL);

        try {
            httpPost.setEntity(new StringEntity(body.toString()));
            HttpResponse response = apacheClient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            statusMessage = response.getStatusLine().getReasonPhrase();
            httpPost.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (statusCode != HTTP_OK)
            throw new HttpResponseException(statusCode, statusMessage);

        return statusCode;
    }
}