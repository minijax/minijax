package org.minijax.s3;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * The UploadUtils class provides helpers for uploading files to S3.
 */
@Provider
@Singleton
public class UploadService {
    private final AmazonS3 s3;


    @Inject
    public UploadService(final AmazonS3 s3) {
        this.s3 = s3;
    }


    /**
     * Uploads a file to S3.
     *
     * File will be publicly accessible.
     *
     * @param bucketName The destination bucket name.
     * @param keyName The destination key.
     * @param file The source content.
     * @return The result URL.
     */
    public String upload(final String bucketName, final String keyName, final File file) throws IOException {
        try {
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setCacheControl("public, max-age=31536000");

            final PutObjectRequest putRequest = new PutObjectRequest(bucketName, keyName, file);
            putRequest.setMetadata(metadata);
            putRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putRequest);

            return "https://" + bucketName + "/" + keyName;

        } catch (final AmazonServiceException ase) {
            // Error response from server
            throw new IOException("AWS service exception: " + ase.getMessage(), ase);

        } catch (final AmazonClientException ace) {
            // Error response in client
            throw new IOException("AWS client exception: " + ace.getMessage(), ace);
        }
    }
}
