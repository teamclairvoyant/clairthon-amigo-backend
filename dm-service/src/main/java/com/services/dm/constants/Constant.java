package com.services.dm.constants;

public class Constant {

    public static final String PATH_SEPARATOR = "/";

    public static final String S3_BUCKET_NAME = "document-manager-resource";

    public static final String UPLOAD_URI = "upload";

    public static final String DOWNLOAD_FILE_URI = "download/{userId}";

    public static final String DOCUMENT_URI = "document";

    public static final String S3_CLIENT = "s3Client";

    public static final String DOCUMENT_TABLE = "ddb-dm-document";

    public static final String STATUS_TABLE = "ddb-dm-document-status";

    public static final String CANDIDATE_TABLE = "ddb-dm-candidate";

    public static final String SUBMIT = "submit";

    public static final String REQUIRED_DOCUMENTS = "requiredDocuments/{candidateId}";

    public static final String UPDATE_STATUS = "updateStatus/{candidateId}/{candidateStatus}";

    public static final String DOCUMENT_LIST_SUBMITTED_SUCCESSFULLY = "Document List Submitted successfully";

    public static final String DOWNLOAD_ALL_FILES_URI = "downloadAll/{userId}";

    public static final String APPLICATION_ZIP = "application/zip";

    public static final String ZIP_FILE = "files.zip";

    public static final String STATUS_UPDATED_SUCCESSFULLY = "Status Updated successfully";

    public static final String PATH_API_REQUESTS = "/api";

    public static final String AUTH_TOKEN_ABSENT = "Auth token is not provided";;

    public static final String AUTHORIZATION = "Authorization";

    public static final String INVALID_TOKEN = "Auth token is invalid or expired";

    public static final CharSequence ISSUER = "https://cognito-idp.ap-south-1.amazonaws.com";
}
