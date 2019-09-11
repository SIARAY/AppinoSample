package ir.ayantech.appinosample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ir.ayantech.appino.Appino;
import ir.ayantech.appino.OperatorName;
import ir.ayantech.appino.PaymentStatusListener;
import ir.ayantech.appino.RechargeListListener;
import ir.ayantech.appino.RechargeModel;
import ir.ayantech.appino.RechargeOrderListener;

public class SimRechargeActivity extends AppCompatActivity {

    private TextView tvOutput;
    private ViewGroup listContainer;
    private EditText etMobileNumber;
    private ProgressDialog progressDialog;
    private AlertDialog paymentDialog;
    private AlertDialog paymentStatusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_credit);
        initUi();
    }

    private void addCardView(final RechargeModel item) {
        View creditItemContainer = getLayoutInflater().inflate(R.layout.item_sim_credit, null);
        TextView tvTitle = creditItemContainer.findViewById(R.id.tvTitle);
        TextView tvAmount = creditItemContainer.findViewById(R.id.tvAmount);
        TextView tvOperator = creditItemContainer.findViewById(R.id.tvOperator);
        TextView tvType = creditItemContainer.findViewById(R.id.tvType);
        tvTitle.setText("عنوان: " + item.getTitle());
        tvAmount.setText("مبلغ: " + item.getAmount());
        tvOperator.setText("اپراتور: " + item.getOperatorType());
        tvType.setText("نوع شارژ: " + item.getRechargeType());
        listContainer.addView(creditItemContainer);
        Log.print("Item added");
        creditItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                Toast.makeText(SimRechargeActivity.this, "" + item.getAmount() + " Clicked", Toast.LENGTH_SHORT).show();
                Appino.rechargePay(item, etMobileNumber.getText().toString(), rechargeOrderListener);
            }
        });
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(SimRechargeActivity.this, "",
                "لطفا کمی صبر کنید...", true);
        progressDialog.setCancelable(false);
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }

    private void initUi() {
        listContainer = findViewById(R.id.listContainer);
        tvOutput = findViewById(R.id.tvOutput);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        tvOutput.setText("Please wait...");
        Appino.getRechargeList(OperatorName.ALL_OPERATORS, rechargeListListener);
    }

    RechargeListListener rechargeListListener = new RechargeListListener() {
        @Override
        public void onSuccess(List<RechargeModel> rechargeList) {
            tvOutput.setText("");

            for (final RechargeModel item : rechargeList
            ) {
                addCardView(item);
            }
        }

        @Override
        public void onFailure(String errorMessage) {
            tvOutput.setText("Error: " + errorMessage);
        }
    };

    RechargeOrderListener rechargeOrderListener = new RechargeOrderListener() {
        @Override
        public void onSuccess(RechargeModel rechargeModel, String orderId) {
            hideProgress();
            Toast.makeText(SimRechargeActivity.this, "Starting purchase page...\n" + orderId, Toast.LENGTH_SHORT).show();
            checkRechargePaymentStatus(orderId);
        }

        @Override
        public void onFailure(String errorMessage) {
            hideProgress();
            Toast.makeText(SimRechargeActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
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

    private void showRechargePaymentStatus(String message) {
        if (paymentStatusDialog != null && paymentStatusDialog.isShowing())
            paymentStatusDialog.dismiss();
        paymentStatusDialog = new AlertDialog.Builder(this)
                .setTitle("جزییات پرداخت")
                .setMessage(message)
                .setPositiveButton("تایید", null)
                .show();
    }

    PaymentStatusListener paymentStatusListener = new PaymentStatusListener() {
        @Override
        public void onSuccess(String amount, String orderId, String transactionId) {
            showRechargePaymentStatus("مبلغ: " + amount + "\n"
                    + "شناسه سفارش: " + orderId + "\n"
                    + "شناسه تراکنش: " + transactionId);
        }

        @Override
        public void onFailure(String errorMessage, String transactionId) {
            //transactionId ممکن است null باشد
            showRechargePaymentStatus(errorMessage);
        }
    };
}
