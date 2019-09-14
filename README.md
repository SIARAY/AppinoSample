## مستندات کتابخانه Appino
### اضافه کردن کتابخانه به برنامه

برای استفاده از کتابخانه اپینو ابتدا باید وابستگی زیر را به برنامه اضافه کنید

```
    implementation 'ir.ayantech:appino:1.0.0'
```

### روش استفاده
پس از ثبت اپلیکیشن خود در پنل اپینو یک توکن به برنامه شما اختصاص داده خواهد شد که شما باید این توکن را به شکل زیر در برنامه اضافه کنید

```
   <meta-data
       android:name="AppinoToken"
       android:value="توکن برنامه شما در این قسمت قرار می گیرد" />
```

سپس کد زیر را در متد onCreate کلای Application
قرار دهید.
```
        Appino.getInstance(context)
                .setDebugEnabled(true)
                .build();
``` 
متد setDebugEnabled جهت مشاهده لاگ های کتابخانه برای اشکال زدایی می باشد که در صورت false شدن کتابخانه هیچ لاگی را چاپ نخواهد کرد.

### بخش شارژ
- دریافت لیست شارژ
 
برای دریافت لیست شارژ از متد زیر استفاده کنید.
پارامتر اول تعیین کننده نوع اپراتور بوده که می تواند یکی از مقادیر ALL_OPERATORS ، HAMRAH_E_AVVAL ، IRANCELL یا RIGHTEL باشد.
```
        Appino.getRechargeList(OperatorName.ALL_OPERATORS, rechargeListListener);
```
برای دریافت نتیجه نیاز به تعریف لیسنر زیر خواهید داشت و باید آنرا به عنوان پارامتر دوم متد بالا ارسال کنید.
```
    RechargeListListener rechargeListListener = new RechargeListListener() {
        @Override
        public void onSuccess(List<RechargeModel> rechargeList) {
        //در صورت دریافت موفقیت آمیز لیست شارژ، این متد فراخوانی می شود
        }

        @Override
        public void onFailure(String errorMessage) {
        //در صورت بروز هر خطایی این متد فراخوانی می شود
        }
    };
```

- ورود به صفحه پرداخت هزینه شارژ

برای ارجاع کاربر به صفحه پرداخت متد زیر را فراخوانی کنید. پس از فراخوانی این متد در صورت موقیت آمیز بودن درخواست، کاربر به صفحه پرداخت منتقل خواهد شد.
rechargeModel مدل شارژی است که در لیست شارژ دریافت شد می باشد
متد دوم شماره موبایلی است که می خواهید شارژ شود.
```
    Appino.rechargePay(rechargeModel, mobileNumber, rechargeOrderListener);
```
متد سوم لیسنر دریافت نتیجه درخواست می باشد که به شکل زیر تعریف می شود.
شناسه سفارش (orderId) بعدا جهت چک کردن وضعیت پرداخت استفاده خواهد شد
```
    RechargeOrderListener rechargeOrderListener = new RechargeOrderListener() {
        @Override
        public void onSuccess(RechargeModel rechargeModel, String orderId) {
        //در صورت موفقیت آمیز بودن ارسال درخواست در این بخش شناسه سفارش دریافت می شود
        }

        @Override
        public void onFailure(String errorMessage) {
        //در صورت بروز هر خطایی این متد فراخوانی می شود
        }
    };
```

- بررسی وضعیت پرداخت

برای بررسی وضعیت پرداخت شارژ یا قبض می توانید از متد زیر استفاده کنید. orderId شناسه پردختی می باشد که در مرحله قبل دریافت شد
```
    Appino.checkPayment(orderId, paymentStatusListener);
```
پارامتر دوم، لیسنر دریافت نتیجه پرداخت می باشد که بصورت زیر تعریف می شود.
مبلغ (amount)، شناسه سفارش (orderId) و شناسه تراکنش (transactionId) در متد onSuccess دریافت خواهد شد. 
```
    PaymentStatusListener paymentStatusListener = new PaymentStatusListener() {
        @Override
        public void onSuccess(String amount, String orderId, String transactionId) {
        //در صورت موفقیت آمیز بودن پرداخت این متد فراخوانی می شود
        }

        @Override
        public void onFailure(String errorMessage, String transactionId) {
        //transactionId ممکن است null باشد
        //در صورت بروز هر خطایی این متد فراخوانی می شود
        }
    };
``` 

## بخش قبض
- پرداخت قبض

از طریق متد زیر می توانید درخواست پرداخت قبض را ارسال نمایید. متد اول شناسه قبض و متد دوم کد پرداخت می باشد.
پس از فراخوانی این متد، در صورت موفقیت آمیز بودن آن کاربر به صفحه پرداخت منتقل می شود.
```
    Appino.billPay(billId, paymentCode, billOrderListener);
```
برای دریافت نتیجه نیاز به تعریف لیسنر زیر می باشد که به عنوان پارامتر سوم متد بالا ارسال می شود.
```
    BillOrderListener billOrderListener = new BillOrderListener() {
        @Override
        public void onSuccess(String BillingId, String paymentCode, String orderId) {
        //در صورت ارسال موفقیت آمیز درخواست پرداخت این متد فراخوانی می شود
        }

        @Override
        public void onFailure(String errorMessage) {
        //در صورت بروز هر خطایی این متد فراخوانی می شود
        }
    };

```

- بررسی وضعیت پرداخت

بررسی وضعیت پرداخت قبض مشابه بررسی پرداخت شارژ می باشد.
