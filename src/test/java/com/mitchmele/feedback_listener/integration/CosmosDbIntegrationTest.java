package com.mitchmele.feedback_listener.integration;

import com.azure.cosmos.CosmosClient;
import org.junit.jupiter.api.*;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.spring.cloud.autoconfigure.implementation.context.AzureGlobalPropertiesAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.implementation.cosmos.AzureCosmosAutoConfiguration;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
@Disabled("CosmosDBEmulatorContainer only offers support for intel based macs - Apple Silicon is not supported.")
class CosmosDbIntegrationTest {

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

    @AfterAll
    static void tearDown() {
        cosmos.stop();
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
