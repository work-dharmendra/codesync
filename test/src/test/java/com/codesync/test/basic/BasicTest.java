package com.codesync.test.basic;

import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.inttest.common.SendForSync;
import com.codesync.test.common.IntTestList;
import com.codesync.test.common.RestClient;
import com.codesync.test.common.TestDto;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class BasicTest {

    private SendForSync sendForSync;
    private String intTestCodeSyncPath = "/Users/dharmendra/code/codesync/inttest/src/main/resources/codesync.json";
    private RestClient restClient;
    private String baseUrl = "http://localhost:8080/inttest/";
    private String id = "inttest";

    public BasicTest() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(new File("src/test/resources/config.properties")));
        restClient = new RestClient(properties.getProperty("inttestwar_url"));
        restClient = new RestClient(baseUrl);

        sendForSync = new SendForSync();
        sendForSync.init(Paths.get(properties.getProperty("codesync_path")));

        id = properties.getProperty("inttestwar_codesync_id");
    }

    @Test
    public void shouldVerifyThatMethodBodyShouldSync() throws IOException {
        TestDto testDto = new TestDto(IntTestList.BASIC_METHOD_BODY_SYNC, "com.codesync.inttest.test.basic.BasicFunctionality"
                , "sync", new Class[]{});

        TestDto result = restClient.post("inttest", testDto);

        Assert.assertEquals("beforeSync", result.result);

        SyncRequestDTO syncRequestDTO = new SyncRequestDTO();

        syncRequestDTO.data = FileUtils.readFileToByteArray(new File("src/test/resources/BasicFunctionality_BASIC_METHOD_BODY_SYNC.class"));
        syncRequestDTO.relativePath = "com/codesync/inttest/test/basic/BasicFunctionality.class";
        syncRequestDTO.fullPath = "fullpath";
        syncRequestDTO.id = id;
        sendForSync.sync(syncRequestDTO);

        result = restClient.post("inttest", testDto);

        System.out.println(result.result);

        Assert.assertEquals("afterSync", result.result);

    }

    @Test
    public void shouldVerifyThatNewClassNewMethodAvailable() throws IOException {
        TestDto testDto = new TestDto(IntTestList.NEWCLASS_NEWMETHOD, "com.codesync.inttest.test.basic.NewClassNewMethod"
                , "newMethod1", new Class[]{});

        TestDto result = restClient.post("inttest", testDto);

        System.out.println(result);
        Assert.assertEquals("beforeSync", result.result);

        SyncRequestDTO syncRequestDTO = new SyncRequestDTO();

        syncRequestDTO.data = FileUtils.readFileToByteArray(new File("src/test/resources/NewClassNewMethod.class"));
        syncRequestDTO.relativePath = "com/codesync/inttest/test/basic/NewClassNewMethod.class";
        syncRequestDTO.fullPath = "fullpath";
        syncRequestDTO.id = id;
        sendForSync.sync(syncRequestDTO);

        result = restClient.post("inttest", testDto);

        System.out.println(result.result);

        Assert.assertEquals("afterSync", result.result);

    }
}
