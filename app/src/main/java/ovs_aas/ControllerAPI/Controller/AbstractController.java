package ovs_aas.ControllerAPI.Controller;

import java.io.IOException;

import org.apache.http.HttpResponse;
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

    public Integer postRequest(String URL, ObjectNode body) {
        int statusCode = 0;
        HttpPost httpPost = new HttpPost(URL);

        System.out.println(body.toString());

        try {
            httpPost.setEntity(new StringEntity(body.toString()));
            HttpResponse response = apacheClient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            httpPost.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statusCode;
    }
}
