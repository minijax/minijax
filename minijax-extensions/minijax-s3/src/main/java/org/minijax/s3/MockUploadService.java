package org.minijax.s3;

import java.io.File;
import java.io.IOException;

public class MockUploadService extends UploadService {

    public MockUploadService() {
        super(null);
    }

    @Override
    public String upload(final String bucketName, final String keyName, final File file) throws IOException {
        return "https://" + bucketName + "/" + keyName;
    }
}
