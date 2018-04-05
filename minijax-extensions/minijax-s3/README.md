minijax-s3
============

The minijax-s3 extension adds convenience methods for uploading content to AWS S3.

This library uses an opinionated model for AWS S3 bucket name and AWS CloudFront configuration:
1) An AWS S3 bucket for storage using the same bucket name as eventual DNS host (i.e., "my-content.example.com")
2) An AWS CloudFront distribution in front of the S3 bucket
3) DNS entries that map the host name to the CloudFront distribution
4) HTTPS-only, requiring SSL certificates

Usage
-----

Prerequisite 1:  You need an AWS account and configured credentials.  Follow these instructions:
* <https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html>

Prerequisite 2:  You need an AWS S3 bucket.  Follow these instructions:
* <https://docs.aws.amazon.com/AmazonS3/latest/gsg/CreatingABucket.html>

Prerequisite 3:  You need a AWS CloudFront distribution in front of the S3 bucket.  Follow these instructions:
* <https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/MigrateS3ToCloudFront.html>

Step 1: Add the `minijax-s3` Maven dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.minijax</groupId>
    <artifactId>minijax-s3</artifactId>
    <version>${minijax.version}</version>
</dependency>
```

Step 2: Inject `UploadService` in resource classes that require uploading:

```java
@Inject
private UploadService uploadService;
```

Step 3: Use the upload service as needed:

```java
uploadService.upload("my-content.example.com", "path/to/my/file.jpg", myFile);
```
