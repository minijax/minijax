package org.minijax.s3;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class UploadServiceTest {
    private UploadService uploadService;


    @Before
    public void setUp() {
        @SuppressWarnings("deprecation")
        final AmazonS3 s3 = new AmazonS3Client() {
            @Override
            public PutObjectResult putObject(final PutObjectRequest putObjectRequest) {
                if (putObjectRequest.getKey().equals("service-exception")) {
                    throw new AmazonServiceException("boom");
                }
                if (putObjectRequest.getKey().equals("client-exception")) {
                    throw new AmazonClientException("boom");
                }
                return new PutObjectResult();
            }
        };

        uploadService = new UploadService(s3);
    }

    @Test
    public void testUpload() throws IOException {
        final File file = mock(File.class);
        uploadService.upload("bucket", "key", file);
    }


    @Test(expected = IOException.class)
    public void testUploadServiceException() throws IOException {
        final File file = mock(File.class);
        uploadService.upload("bucket", "service-exception", file);
    }


    @Test(expected = IOException.class)
    public void testUploadClientException() throws IOException {
        final File file = mock(File.class);
        uploadService.upload("bucket", "client-exception", file);
    }
}
