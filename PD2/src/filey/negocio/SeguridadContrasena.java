package filey.negocio;

import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class SeguridadContrasena {
    private static final int ITERACIONES=120000;
    private static final SecureRandom AZAR=new SecureRandom();
    static String hash(char[] clave) {
        byte[] sal=new byte[16]; AZAR.nextBytes(sal);
        return ITERACIONES+":"+Base64.getEncoder().encodeToString(sal)+":"+
               Base64.getEncoder().encodeToString(derivar(clave,sal,ITERACIONES));
    }
    static boolean verificar(char[] clave,String codificado) {
        try {
            String[] partes=codificado.split(":");
            int iteraciones=Integer.parseInt(partes[0]);
            if (iteraciones<10000 || iteraciones>1000000) return false;
            byte[] esperada=Base64.getDecoder().decode(partes[2]);
            byte[] calculada=derivar(clave,Base64.getDecoder().decode(partes[1]),iteraciones);
            return MessageDigest.isEqual(esperada,calculada);
        } catch (RuntimeException e) {return false;}
    }
    private static byte[] derivar(char[] clave,byte[] sal,int iteraciones) {
        PBEKeySpec spec=new PBEKeySpec(clave,sal,iteraciones,256);
        try {return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();}
        catch (Exception e) {throw new IllegalStateException("No se pudo derivar clave",e);}
        finally {spec.clearPassword();}
    }
    private SeguridadContrasena() {}
}
