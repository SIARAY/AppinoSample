package ir.ayantech.appinosample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ir.ayantech.appino.Appino;
import ir.ayantech.appino.OrderType;
import ir.ayantech.appino.PaymentCallback;
import ir.ayantech.appino.StoreOptions;
import ir.ayantech.appino.StoreType;

import static ir.ayantech.appinosample.Utils.getTypeface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 32;
    private Button btnRecharge;
    private Button btnBill;
    private Button btnStore;
    private Button btnStoreBottomSheet;
    private AlertDialog paymentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestPermissions();
        initializeUi();
    }

    private void initializeUi() {
        TextView tvLabel = findViewById(R.id.tvLabel);
        tvLabel.setText(getString(R.string.appino_sdk_label, BuildConfig.VERSION_NAME)
                + "\n" + "نسخه آزمایشی 4");
        btnRecharge = findViewById(R.id.btnRecharge);
        btnBill = findViewById(R.id.btnBill);
        btnStore = findViewById(R.id.btnStore);
        btnStoreBottomSheet = findViewById(R.id.btnStoreBottomSheet);
        btnRecharge.setOnClickListener(this);
        btnBill.setOnClickListener(this);
        btnStore.setOnClickListener(this);
        btnStoreBottomSheet.setOnClickListener(this);
        btnRecharge.setTypeface(getTypeface(this));
        btnBill.setTypeface(getTypeface(this));
        btnStore.setTypeface(getTypeface(this));
        btnStoreBottomSheet.setTypeface(getTypeface(this));
    }


    @Override
    public void onClick(View v) {
        StoreOptions options = StoreOptions.getInstance()
                .setTypeface(getTypeface(this))
                .setTextSizeRatio(1.0f)
                .setCallBack(new PaymentCallback() {
                    @Override
                    public void onSuccess(String orderType, String orderId, String transactionId, int value) {
                        showPaymentStatusDialog(true, orderType, orderId, transactionId, value);
                    }

                    @Override
                    public void onFailure(String orderType, String orderId) {
                        showPaymentStatusDialog(false, orderType, orderId, "", 0);
                    }
                });
        switch (v.getId()) {

            case R.id.btnStore:
                options.setType(StoreType.FULLSCREEN);
                Appino.startStore(options);
                break;

            case R.id.btnStoreBottomSheet:
                options.setType(StoreType.BOTTOMSHEET);
                Appino.startStore(options);
                break;
        }
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private void showPaymentStatusDialog(boolean success, String orderType, String orderId, String transactionId, int value) {
        if (paymentDialog != null && paymentDialog.isShowing())
            paymentDialog.dismiss();
        String statusMessage = "پرداخت انجام نشد";
        String type;
        if (success)
            statusMessage = "پرداخت موفقیت آمیز";
        if (orderType == OrderType.RECHARGE) {
            type = "شارژ";
        } else if (orderType == OrderType.INTERNET_PACKAGE) {
            type = "بسته اینترنت";
        } else {
            type = "قبض";
        }
        paymentDialog = new AlertDialog.Builder(this)
                .setTitle("مقادیر دریافت شده در برنامه")
                .setMessage("وضعیت: " + statusMessage + "\n"
                        + "نوع: " + type + "\n"
                        + "شناسه سفارش: " + orderId + "\n"
                        + "ارزش: " + value + "\n"
                        + "کد پیگیری: " + transactionId)
                .setPositiveButton("تایید", null)
                .show();
    }
}
