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
            String base64EncodedPublicKey = "";
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
