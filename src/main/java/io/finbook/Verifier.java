package io.finbook;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

public class Verifier {

    public byte[] textToValidate;

    public Verifier(byte[] textToValidate) {
        this.textToValidate = textToValidate;
    }

    public Verifier(String path) {
        this.textToValidate = new FileHandler(path).readByteArray();
    }

    public String validateSign() {
        Security.addProvider(new BouncyCastleProvider());

        String rfc = null;

        CMSSignedData cms;
        if((cms = validateContentOf(textToValidate)) == null) {
            return null;
        }

        if(cms.getSignerInfos().getSigners().size() == 0) {
            return null;
        }

        Store<X509CertificateHolder> store = cms.getCertificates();

        for (SignerInformation signer : cms.getSignerInfos().getSigners()) {

            X509Certificate cert = getCertOf(signer, store);
            if(cert == null) {
                return null;
            }

            if(validateCAOf(signer, cert)) {
                if(rfc == null) rfc = getRFCOf(cert.getSubjectDN().toString());
            } else {
                return null;
            }
        }

        if(rfc == null) {
            throw new RuntimeException("El certificado con el que se ha firmado no dispone de un campo RFC");
        }

        return rfc;
    }

    private String getRFCOf(String subject) {
        for (String field : subject.split(",")) {
            if(field.substring(0, field.indexOf('=')).equals("1.3.5.8")) {
                return field.substring(field.indexOf('=') + 1);
            }
        }

        return null;
    }

    private X509Certificate getCertOf(SignerInformation signer, Store<X509CertificateHolder> store) {
        Collection certCollection = store.getMatches(signer.getSID());
        Iterator certIt = certCollection.iterator();
        X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
        try {
            return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
        } catch (CertificateException e) {
            return null;
        }
    }

    private CMSSignedData validateContentOf(byte[] textToValidate) {
        try {
            return new CMSSignedData(textToValidate);
        } catch(Exception e) {
            throw new RuntimeException("El contenido de la firma no ha podido ser validado");
        }
    }

    private boolean validateCAOf(SignerInformation signer, X509Certificate cert) {
        try {
            return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert));
        } catch(Exception e) {
            throw new RuntimeException("El certificado no está firmado por una entidad de confianza");
        }
    }
}
