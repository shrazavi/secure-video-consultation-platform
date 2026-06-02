package com.shrazavi.dadmehr.core.util;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyGenParameterSpec.Builder;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.LoadStoreParameter;
import java.util.Arrays;
import javax.crypto.KeyGenerator;

public final class MasterKeys {
    private static final int KEY_SIZE = 256;
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    static final String KEYSTORE_PATH_URI = "android-keystore://";
    static final String MASTER_KEY_ALIAS = "_androidx_security_master_key_";
    @NonNull
    public static final KeyGenParameterSpec AES256_GCM_SPEC = createAES256GCMKeyGenParameterSpec("_androidx_security_master_key_");

    public MasterKeys() {
    }

    @NonNull
    private static KeyGenParameterSpec createAES256GCMKeyGenParameterSpec(@NonNull String keyAlias) {
        Builder builder = (new Builder(keyAlias, 3)).setBlockModes(new String[]{"GCM"}).setEncryptionPaddings(new String[]{"NoPadding"}).setKeySize(256);
        return builder.build();
    }

    @NonNull
    private static KeyGenParameterSpec createAES256GCMKeyGenParameterSpec() {
        return createAES256GCMKeyGenParameterSpec("_androidx_security_master_key_");
    }

    @NonNull
    public static String getOrCreate(@NonNull KeyGenParameterSpec keyGenParameterSpec) throws GeneralSecurityException, IOException {
        validate(keyGenParameterSpec);
        if (!keyExists(keyGenParameterSpec.getKeystoreAlias())) {
            generateKey(keyGenParameterSpec);
        }

        return keyGenParameterSpec.getKeystoreAlias();
    }

    private static void validate(KeyGenParameterSpec spec) {
        if (spec.getKeySize() != 256) {
            throw new IllegalArgumentException("invalid key size, want 256 bits got " + spec.getKeySize() + " bits");
        } else if (spec.getBlockModes().equals(new String[]{"GCM"})) {
            throw new IllegalArgumentException("invalid block mode, want GCM got " + Arrays.toString(spec.getBlockModes()));
        } else if (spec.getPurposes() != 3) {
            throw new IllegalArgumentException("invalid purposes mode, want PURPOSE_ENCRYPT | PURPOSE_DECRYPT got " + spec.getPurposes());
        } else if (spec.getEncryptionPaddings().equals(new String[]{"NoPadding"})) {
            throw new IllegalArgumentException("invalid padding mode, want NoPadding got " + Arrays.toString(spec.getEncryptionPaddings()));
        }
    }

    private static void generateKey(@NonNull KeyGenParameterSpec keyGenParameterSpec) throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    private static boolean keyExists(@NonNull String keyAlias) throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load((LoadStoreParameter)null);
        return keyStore.containsAlias(keyAlias);
    }
}