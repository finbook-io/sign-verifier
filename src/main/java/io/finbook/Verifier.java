package io.finbook;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ContentVerifierProviderBuilder;
import org.bouncycastle.cert.path.CertPath;
import org.bouncycastle.cert.path.CertPathValidation;
import org.bouncycastle.cert.path.validations.BasicConstraintsValidation;
import org.bouncycastle.cert.path.validations.CertificatePoliciesValidationBuilder;
import org.bouncycastle.cert.path.validations.KeyUsageValidation;
import org.bouncycastle.cert.path.validations.ParentCertIssuedValidation;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Verifier {

    public byte[] textToValidate;

    public Verifier(byte[] textToValidate) {
        this.textToValidate = textToValidate;
    }

    public Verifier(String path) {
        this.textToValidate = new FileHandler(path).readByteArray();
    }

    public boolean validateSign() throws CertificateException, OperatorCreationException, IOException, CMSException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(textToValidate);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));

        Store<X509CertificateHolder> store = cmsSignedData.getCertificates();

        for (SignerInformation signer : cmsSignedData.getSignerInfos().getSigners()) {
            X509CertificateHolder certHolder = (X509CertificateHolder) store.getMatches(signer.getSID()).iterator().next();
            if(verifyCertificateIntegrity(certHolder)) {
                Security.addProvider(new BouncyCastleProvider());
                X509Certificate cert = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(certHolder);
                return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build(cert));
            }
        }
        return false;
    }

    private boolean verifyCertificateIntegrity(X509CertificateHolder certHolder) {
        CertPath path = new CertPath(new X509CertificateHolder[] {certHolder});
        CertPathValidation[] params = new CertPathValidation[] {
                new ParentCertIssuedValidation(new JcaX509ContentVerifierProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME)),
                new KeyUsageValidation(),
                new BasicConstraintsValidation(),
                new CertificatePoliciesValidationBuilder().build(path)
        };

        try {
            return path.validate(params).isValid();
        } catch(NullPointerException e) {
            return false;
        }
    }
}
