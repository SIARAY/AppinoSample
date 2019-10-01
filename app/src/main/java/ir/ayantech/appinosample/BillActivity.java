package ir.ayantech.appinosample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ir.ayantech.appino.Appino;
import ir.ayantech.appino.BillOrderListener;
import ir.ayantech.appino.PaymentStatusListener;

public class BillActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etBillId;
    private EditText etPaymentCode;
    private ProgressDialog progressDialog;
    private AlertDialog paymentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initUi();
    }

    private void initUi() {
        Button btnSend = findViewById(R.id.btnSend);
        etBillId = findViewById(R.id.etBillId);
        etPaymentCode = findViewById(R.id.etPaymentCode);
        btnSend.setOnClickListener(this);
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(BillActivity.this, "",
                "Loading. Please wait...", true);
        progressDialog.setCancelable(false);
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                showProgress();
                Appino.billPay(etBillId.getText().toString()
                        , etPaymentCode.getText().toString()
                        , billOrderListener);
                break;
        }
    }

    BillOrderListener billOrderListener = new BillOrderListener() {
        @Override
        public void onSuccess(String BillingId, String paymentCode, String orderId) {
            hideProgress();
            Toast.makeText(BillActivity.this, "Payment page started...\n" + orderId, Toast.LENGTH_SHORT).show();
            Log.print("OrderId: "+orderId);
            checkRechargePaymentStatus(orderId);
        }

        @Override
        public void onFailure(String errorMessage) {
            hideProgress();
            showMessageDialog("Error: " + errorMessage);
        }
    };

    private void checkRechargePaymentStatus(final String orderId) {
        if (paymentDialog != null && paymentDialog.isShowing())
            paymentDialog.dismiss();
        paymentDialog = new AlertDialog.Builder(this)
                .setTitle("بررسی وضعیت پرداخت")
                .setMessage("شما می توانید از طریق شناسه سفارش، وضعیت پرداخت را بررسی نمایید")
                .setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Appino.checkPayment(orderId, paymentStatusListener);
                    }
                })
                .setNegativeButton("لغو", null)
                .show();
    }

    PaymentStatusListener paymentStatusListener = new PaymentStatusListener() {
        @Override
        public void onSuccess(int value, String amount, String currency, String orderId, String transactionId, String date) {
            showMessageDialog("مبلغ: " + amount + "\n"
                    + "شناسه سفارش: " + orderId + "\n"
                    + "شناسه تراکنش: " + transactionId);
        }

        @Override
        public void onFailure(String errorMessage, String transactionId) {
            //transactionId ممکن است null باشد
            showMessageDialog(errorMessage);
        }
    };

    private void showMessageDialog(String message) {
        if (paymentDialog != null && paymentDialog.isShowing())
            paymentDialog.dismiss();
        paymentDialog = new AlertDialog.Builder(this)
                .setTitle("جزییات پرداخت")
                .setMessage(message)
                .setPositiveButton("تایید", null)
                .show();
    }
}
