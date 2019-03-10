package sound.chill.com.mychilloutplayer.billing;

import android.annotation.SuppressLint;
import android.app.Application;

import org.solovyev.android.checkout.Billing;

@SuppressLint("Registered")
public class CheckoutApplication extends Application {

    private static CheckoutApplication sInstance;

    private final Billing mBilling = new Billing(this, new Billing.DefaultConfiguration() {
        @Override
        public String getPublicKey() {
            String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArNpm3hrdmWXTXhGwTQK1e37coc+kPfn5xKp2wIDpXhJECIdBeEuNiBFGCKJxLgN4vhgtnAOziB7ExYBMvwh8cAClNi7tImQIsBgc+2ca9F1InbCxSyFn4Umtf4NK1BIND457GN0byc3AEwh2/VxCnuhnaZlonh8cCtlyjQdd4AaUsuS/yLSXgM/F88GJ0DR896Sy4SIj5z/GEFoSQ5IU3weot2ziDgGO18XhTgEGZT1AB2Q/ixAlQbUlNSANczdd1K+oCGf9mFfG2sq1eGLYCjkDVXi1uleCNty0CUzSvutQVx/J/ovi0YwBbVePJ2L3/d5W/Ku0doRdi5PZ4xB+mwIDAQAB";
            return Encryption.decrypt(base64EncodedPublicKey, "grisha.gorbaba@gmail.com");
        }
    });

    public CheckoutApplication() {
        sInstance = this;
    }

    public static CheckoutApplication get(SkuActivity skuActivity) {
        return sInstance;
    }

    public Billing getBilling() {
        return mBilling;
    }
}
