package com.mitchmele.feedback_listener;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.cosmos.AzureCosmosAutoConfiguration;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.CosmosDBEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;


@Tag("integrationTest")
@SpringBootTest
@Testcontainers
@ImportAutoConfiguration(classes = {
        AzureGlobalPropertiesAutoConfiguration.class,
        AzureCosmosAutoConfiguration.class
})
class CosmosDbIntegrationTest {

    //change to junit 5 again

    @TempDir
    private static File tempFolder;

    @Autowired
    private CosmosClient cosmosClient;

    @Container
    @ServiceConnection
    static CosmosDBEmulatorContainer cosmos = new CosmosDBEmulatorContainer(
            DockerImageName.parse("mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest")
    );

    @BeforeAll
    static void setUp()  {
        cosmos.start();
        Path keyStoreFile = new File(tempFolder, "azure-cosmos-emulator.keystore").toPath();
        KeyStore keyStore = cosmos.buildNewKeyStore();
        try {
            keyStore.store(Files.newOutputStream(keyStoreFile.toFile().toPath()), cosmos.getEmulatorKey().toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", cosmos.getEmulatorKey());
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
    }

//    @BeforeEach
//    public void setClient() throws Exception {
//        cosmosClient = new CosmosClientBuilder()
//                .gatewayMode()
//                .endpointDiscoveryEnabled(false)
//                .endpoint(cosmos.getEmulatorEndpoint())
//                .key(cosmos.getEmulatorKey())
//                .buildClient();
//    }


    @Test
    void contextLoads() {

    }

    @Test
    void canConnectToDockerDb() {
        CosmosDatabaseResponse res = cosmosClient.createDatabaseIfNotExists("azure-test");
        assertThat(res.getStatusCode()).isEqualTo(201);

        CosmosContainerResponse cosmosContainerResponse = cosmosClient.getDatabase("azure-test")
                .createContainerIfNotExists("feedback", "/email");

        assertThat(cosmosContainerResponse.getStatusCode()).isEqualTo(201);
    }
}
