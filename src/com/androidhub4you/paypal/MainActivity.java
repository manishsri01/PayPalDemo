package com.androidhub4you.paypal;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button btnPay;
	//set the environment for production/sandbox/no netowrk
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    private static final String CONFIG_CLIENT_ID = "PUT YOUR PAYPAL CLIENT ID";

    private static final int REQUEST_PAYPAL_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Android Hub 4 You")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnPay=(Button)findViewById(R.id.button1);
		btnPay.setOnClickListener(this);

		/**
		 * call pay pal services
		 */
		
		Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1 :
			 PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(1),"USD", "androidhub4you.com",
			            PayPalPayment.PAYMENT_INTENT_SALE);

			        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);

			        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

			        startActivityForResult(intent, REQUEST_PAYPAL_PAYMENT);	
			break;
		}
		
	}
	
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_PAYPAL_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PaymentConfirmation confirm = data
	                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	                if (confirm != null) {
	                    try {
	                    	System.out.println("Responseeee"+confirm);
	                        Log.i("paymentExample", confirm.toJSONObject().toString());

	                      
	                        JSONObject jsonObj=new JSONObject(confirm.toJSONObject().toString());
	                        
	                        String paymentId=jsonObj.getJSONObject("response").getString("id");
	                        System.out.println("payment id:-=="+paymentId);
	                        Toast.makeText(getApplicationContext(), paymentId, Toast.LENGTH_LONG).show();  

	                    } catch (JSONException e) {
	                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("paymentExample", "The user canceled.");
	            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
	            }
	        } 
	        
	         
	  }



	
}
