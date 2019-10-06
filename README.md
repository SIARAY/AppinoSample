## مستندات کتابخانه Appino
### اضافه کردن کتابخانه به برنامه

برای استفاده از کتابخانه اپینو ابتدا باید وابستگی زیر را به برنامه اضافه کنید

```
    implementation 'ir.ayantech:appino:1.0.1'
```

### روش استفاده
پس از ثبت اپلیکیشن خود در پنل اپینو یک توکن به برنامه شما اختصاص داده خواهد شد که شما باید این توکن را به شکل زیر در تگ application مانیفست برنامه خود اضافه کنید

```xml
    <manifest>
        <application>
        
            <!-- بخش زیر به مانیفست اضافه شود-->
            <meta-data
               android:name="AppinoToken"
               android:value="توکن برنامه شما در این قسمت قرار می گیرد" />
        
        </application>
    </manifest>
```

در صورتی که برنامه شما پروگارد شده است، کد زیر را به فایل proguard-rules برنامه خود اضافه کنید
```proguard
-keep class ir.ayantech.appino.** { *; }
```


برای استفاده از این کتابخانه باید مجوز های زیر را در مانیفست برنامه خود قرار دهید.
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.CAMERA" />
```
مجوز مخاطبین جهت وارد کردن شماره از لیست مخاطبین و مجوز دوربین برای بارکدخوان در بخش فروشگاه مورد نیاز است. در صورتی که از این بخش استفاده نمی کنید می توانید این مجوز ها را حذف کنید.


سپس کد زیر را در متد onCreate کلاس Application
قرار دهید.
```java
        Appino.getInstance(context)
                .setDebugEnabled(true)
                .setTypeface(typeface)
                .build();
``` 
متد setDebugEnabled جهت مشاهده لاگ های کتابخانه برای اشکال زدایی می باشد که در صورت false شدن کتابخانه هیچ لاگی را چاپ نخواهد کرد.
متد setTypeface برای شخصی سازی فونت استفاده شده در بخش فروشگاه می باشد.


### بخش فروشگاه
بخش فروشگاه استفاده از کتابخانه را آسان نموده و در صورتی که نیاز به شخصی سازی رابط کاربری ندارید می توانید از این بخش استفاده کنید.

ابتدا اکتیویتی زیر را به بخش application فایل مانیفست برنامه خود اضافه کنید
```xml
        <activity android:name="ir.ayantech.appino.AppinoActivity"
            android:screenOrientation="portrait"/>
```
می توانید با فراخوانی مند زیر از تمام امکانات کتابخانه استفاده نمایید

```java
        Appino.startStore(YourActivity.this, new PaymentCallback() {
            @Override
            public void onSuccess(String orderType, String orderId, String transactionId, int value) {
                //زمانی که پرداخت با موفقیت انجام شده باشد این متد فراخوانی می شود
            }
    
            @Override
            public void onFailure(String orderType, String orderId) {
                //در صورت خطا در پرداخت این متد فراخوانی می شود
            }
        });
```
پس از فراخوانی متد بالا وارد اکتیویتی خرید شارژ و پرداخت قبض خواهید شد.

پس از هر خرید شارژ یا پرداخت قبض با توجه به موفق بودن یا نبودن آن یکی از متد های onSuccess یا onFailure فراخوانی می شود.

فیلد orderType مشخص کننده نوع قبض یا شارژ می باشد که می توانید در اینترفیس OrderType مقادیر آنرا مشاهده نمایید.

فیلد orderId شناسه سفارش می باشد.

فیلد transactionId شناسه تراکنش یا کد پیگیری می باشد.

فیلد value برای خرید شارژ کاربرد دارد که با توجه به مبلغ شارژ مقدار متفاوتی را ارسال می کند.

چنانچه در برنامه خود نیاز به در نظر گرفتن جایزه(یا سکه) برای کاربر دارید می توانید از فیلد value استفاده کنید.


### بخش شارژ
- دریافت لیست شارژ
 
برای دریافت لیست شارژ از متد زیر استفاده کنید.
پارامتر اول تعیین کننده نوع اپراتور بوده که می تواند یکی از مقادیر ALL_OPERATORS ، HAMRAH_E_AVVAL ، IRANCELL یا RIGHTEL باشد.

```java
        Appino.getRechargeList(OperatorName.ALL_OPERATORS, rechargeListListener);
```

برای دریافت نتیجه نیاز به تعریف لیسنر زیر خواهید داشت و باید آنرا به عنوان پارامتر دوم متد بالا ارسال کنید.

```java
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
```java
    Appino.rechargePay(rechargeModel, mobileNumber, rechargeOrderListener);
```
متد سوم لیسنر دریافت نتیجه درخواست می باشد که به شکل زیر تعریف می شود.
شناسه سفارش (orderId) بعدا جهت چک کردن وضعیت پرداخت استفاده خواهد شد
```java
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
```java
    Appino.checkPayment(orderId, paymentStatusListener);
```
پارامتر دوم، لیسنر دریافت نتیجه پرداخت می باشد که بصورت زیر تعریف می شود.
مبلغ (amount)، شناسه سفارش (orderId) و شناسه تراکنش (transactionId) در متد onSuccess دریافت خواهد شد. 
```java
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

از طریق متد زیر می توانید درخواست پرداخت قبض را ارسال نمایید. پارامتر اول شناسه قبض و پارامتر دوم کد پرداخت می باشد.
پس از فراخوانی این متد، در صورت موفقیت آمیز بودن آن کاربر به صفحه پرداخت منتقل می شود.
```java
    Appino.billPay(billId, paymentCode, billOrderListener);
```
برای دریافت نتیجه نیاز به تعریف لیسنر زیر می باشد که به عنوان پارامتر سوم متد بالا ارسال می شود.
```java
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
