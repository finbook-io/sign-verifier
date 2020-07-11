import io.finbook.Verifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Base64;

import static org.junit.Assert.*;

@RunWith(org.junit.runners.Parameterized.class)
public class Verifier_ {

    private static Object[][] cases;

    static {
        cases = new Object[][] {
                {
                        "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",
                        "RCO2816104T1",
                        null,
                        null
                },
                {
                        "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",
                        "RCO2816104T1",
                        RuntimeException.class,
                        "El contenido de la firma no ha podido ser validado"
                },
                {
                        "MIAGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBwGggCSABAR0ZXN0AAAAAAAAoIAwggM+MIICJqADAgECAgkA+93WDHXgLcIwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCTUExEDAOBgNVBAgTB01vcm9jY28xEzARBgNVBAcTCkNhc2FibGFuY2ExETAPBgNVBAMTCEJhZWxkdW5nMB4XDTE3MTAxMjEwNDMxNFoXDTI3MTAxMzEwNDMxNFowRzELMAkGA1UEBhMCTUExEDAOBgNVBAgTB01vcm9jY28xEzARBgNVBAcTCkNhc2FibGFuY2ExETAPBgNVBAMTCEJhZWxkdW5nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzKLkaY543hBof8I/mBI7IdfzOdvlMMdZXzBz5lPuQ1TzO3m0YyGOjMrqFrHXnn/q06wENHStJgGEv4Ob/64JVLuo6VEeF/pw0a3MYXWCc0kHZaMjygwBibbjGkuPVamEpTAqufedMw+dC6nk6g0AxiX31+UA2tFhWOPGstC8NkNSKjoi6OsJnZO1cYzSD+A/77bwpPuDUqVS6LfG0HS7ffDHtQ3wWLHcmaM5o8cbDEanA/dUZKN+zxy6GpXBFBpmkI6gdT8/zClcrDzSxJ2HP5JG54w+1ViorQKIsvn87SHRjeV7ghXCGCDcobWJMY5afrRQwWfURiZDXyTk2yZrpQIDAQABoy0wKzAJBgNVHRMEAjAAMBEGCWCGSAGG+EIBAQQEAwIE8DALBgNVHQ8EBAMCBSAwDQYJKoZIhvcNAQEFBQADggEBALwMMGYnenjHK3EGITSzinXvXlZPoOb9AkenRcLSGRKjJkoOH5dWavOb4ndKJbW+mzBvmEuA+jTjRhf2OcOaA4P5OmV5O4ofFlFiP3ZRhaZ2ORYtW0Uxy86H6DEGIA+kcipmyqjyV9zIC6HwnHzfghduSd8JS0REMsb+tT0//dcmGszYYLmo9DOetbgRiFQIY/Iglea8BMxW9cxYK1+iAAv6fja+NhdBdmgBaiYeEseg1tIlQSuc11YM6Hhagm1ckLEDpTktIGmWpvfAHaFROrD7Rp2YqW2+bL4oDU8Ps7GgaJjtXnjjN4aolqbWrFSJrvLkEvq45r/hKFJXylwkNjMAADGCAhowggIWAgEBMFQwRzELMAkGA1UEBhMCTUExEDAOBgNVBAgTB01vcm9jY28xEzARBgNVBAcTCkNhc2FibGFuY2ExETAPBgNVBAMTCEJhZWxkdW5nAgkA+93WDHXgLcIwDQYJYIZIAWUDBAIBBQCggZgwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMjAwNzExMTIwOTMwWjAtBgkqhkiG9w0BCTQxIDAeMA0GCWCGSAFlAwQCAQUAoQ0GCSqGSIb3DQEBCwUAMC8GCSqGSIb3DQEJBDEiBCCfhtCBiEx9ZZov6qDFWtAVo79PGysLgizRXWwVsPAKCDANBgkqhkiG9w0BAQsFAASCAQDKVxV4OGcM5Go0Gjm09wsTuHj0x4CPwH6C3VKSib5mkKyZS6Ow27ni4Qfeg5g/JMJjfX7zk4IaJRv+x1nwFcbXHmzpjbSC47PgCREOLz3Zkg4yWZlFUjzfyvpjn98k4fNXAyjfgtsamUZnzuVHu2VVXXfDEj+tCFCNETssyTe8l/Lv1CMLtmYOJgjLPsflonTlofKDYh5Lg60kVt3/cExlH/SMkNAWTnkHLWsGRx9qC/KPfyzUerINkcsfIKlIXvdgPkDZRbzkCKs4rolOuqafFITzG1fYueVcXt3FA0Z2m4CN7TkhICBqzDzXXHlPsf3SUNqkrZNFigGveCjWC1FqAAAAAAAA",
                        null,
                        RuntimeException.class,
                        "El certificado con el que se ha firmado no dispone de un campo RFC"
                },
                {
                        "L4AGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBwGggCSABAR0ZXN0AAAAAAAAoIAwggM+MIICJqADAgECAgkA+93WDHXgLcIwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCTUExEDAOBgNVBAgTB01vcm9jY28xEzARBgNVBAcTCkNhc2FibGFuY2ExETAPBgNVBAMTCEJhZWxkdW5nMB4XDTE3MTAxMjEwNDMxNFoXDTI3MTAxMzEwNDMxNFowRzELMAkGA1UEBhMCTUExEDAOBgNVBAgTB01vcm9jY28xEzARBgNVBAcTCkNhc2FibGFuY2ExETAPBgNVBAMTCEJhZWxkdW5nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzKLkaY543hBof8I/mBI7IdfzOdvlMMdZXzBz5lPuQ1TzO3m0YyGOjMrqFrHXnn/q06wENHStJgGEv4Ob/64JVLuo6VEeF/pw0a3MYXWCc0kHZaMjygwBibbjGkuPVamEpTAqufedMw+dC6nk6g0AxiX31+UA2tFhWOPGstC8NkNSKjoi6OsJnZO1cYzSD+A/77bwpPuDUqVS6LfG0HS7ffDHtQ3wWLHcmaM5o8cbDEanA/dUZKN+zxy6GpXBFBpmkI6gdT8/zClcrDzSxJ2HP5JG54w+1ViorQKIsvn87SHRjeV7ghXCGCDcobWJMY5afrRQwWfURiZDXyTk2yZrpQIDAQABoy0wKzAJBgNVHRMEAjAAMBEGCWCGSAGG+EIBAQQEAwIE8DALBgNVHQ8EBAMCBSAwDQYJKoZIhvcNAQEFBQADggEBALwMMGYnenjHK3EGITSzinXvXlZPoOb9AkenRcLSGRKjJkoOH5dWavOb4ndKJbW+mzBvmEuA+jTjRhf2OcOaA4P5OmV5O4ofFlFiP3ZRhaZ2ORYtW0Uxy86H6DEGIA+kcipmyqjyV9zIC6HwnHzfghduSd8JS0REMsb+tT0//dcmGszYYLmo9DOetbgRiFQIY/Iglea8BMxW9cxYK1+iAAv6fja+NhdBdmgBaiYeEseg1tIlQSuc11YM6Hhagm1ckLEDpTktIGmWpvfAHaFROrD7Rp2YqW2+bL4oDU8Ps7GgaJjtXnjjN4aolqbWrFSJrvLkEvq45r/hKFJXylwkNjMAADGCAhowggIWAgEBMFQwRzELMAkGA1UEBhMCTUExEDAOBgNVBAgTB01vcm9jY28xEzARBgNVBAcTCkNhc2FibGFuY2ExETAPBgNVBAMTCEJhZWxkdW5nAgkA+93WDHXgLcIwDQYJYIZIAWUDBAIBBQCggZgwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMjAwNzExMTIwOTMwWjAtBgkqhkiG9w0BCTQxIDAeMA0GCWCGSAFlAwQCAQUAoQ0GCSqGSIb3DQEBCwUAMC8GCSqGSIb3DQEJBDEiBCCfhtCBiEx9ZZov6qDFWtAVo79PGysLgizRXWwVsPAKCDANBgkqhkiG9w0BAQsFAASCAQDKVxV4OGcM5Go0Gjm09wsTuHj0x4CPwH6C3VKSib5mkKyZS6Ow27ni4Qfeg5g/JMJjfX7zk4IaJRv+x1nwFcbXHmzpjbSC47PgCREOLz3Zkg4yWZlFUjzfyvpjn98k4fNXAyjfgtsamUZnzuVHu2VVXXfDEj+tCFCNETssyTe8l/Lv1CMLtmYOJgjLPsflonTlofKDYh5Lg60kVt3/cExlH/SMkNAWTnkHLWsGRx9qC/KPfyzUerINkcsfIKlIXvdgPkDZRbzkCKs4rolOuqafFITzG1fYueVcXt3FA0Z2m4CN7TkhICBqzDzXXHlPsf3SUNqkrZNFigGveCjWC1FqAAAAAAAA",
                        null,
                        RuntimeException.class,
                        "El contenido de la firma no ha podido ser validado"
                },
                {
                        "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",
                        null,
                        RuntimeException.class,
                        "El certificado no está firmado por una entidad de confianza"
                }
        };
    }

    private String sign;
    private String RFC;
    private Object exceptionClass;
    private String exceptionMessage;

    public Verifier_(String sign, String RFC, Object exceptionClass, String exceptionMessage) {
        this.sign = sign;
        this.RFC = RFC;
        this.exceptionClass = exceptionClass;
        this.exceptionMessage = exceptionMessage;
    }

    @Test
    public void execute(){
        try {
            assertEquals(RFC, new Verifier(Base64.getDecoder().decode(sign.getBytes())).validateSign());
            assertNull(exceptionClass);
        } catch(RuntimeException e) {
            assertEquals(exceptionMessage, e.getMessage());
            assertNotNull(exceptionClass);
        }
    }

    @Parameterized.Parameters
    public static Object[][] cases() {
        return cases;
    }
}
