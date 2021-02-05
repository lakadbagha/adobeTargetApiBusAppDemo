/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2018 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by all applicable intellectual property
 * laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package adobe.bus.booking.demo.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.TargetRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import static adobe.bus.booking.demo.app.AppContsant.MBOX_NAME;


/**
 * This activity class is responsible to show booking engine page and offer card.
 */
public class BusBookingActivity extends AppCompatActivity {

    private TextView mTextGoingTo, mTextLeavingFrom, mTextSource, mTextDestination;
    private ImageButton mBtnFlip;
    private LinearLayout linearLayout;
    private ImageView webView, showBus;
    private TextView text_offer_title, text_offer_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_booking);
        setUpToolBar();
        mTextGoingTo = findViewById(R.id.text_going_to);
        mTextLeavingFrom = findViewById(R.id.text_leaving_from);
        mTextDestination = findViewById(R.id.text_destination);
        mTextSource = findViewById(R.id.text_source);
        mBtnFlip = findViewById(R.id.btn_flip);
        linearLayout = findViewById(R.id.lin_date_seat_container);
        webView = findViewById(R.id.webView);
        showBus = findViewById(R.id.showBus);
        text_offer_title = findViewById(R.id.text_offer_title);
        text_offer_desc = findViewById(R.id.text_offer_desc);
        mBtnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flipSourceDesti();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("text", "default");
                    jsonObject.put("url", "https://webanalyticsfordevelopers.com");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Snackbar.make(linearLayout, "Creating Target request...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                TargetRequest targetRequest1 = new TargetRequest(MBOX_NAME, null, jsonObject.toString(), new AdobeCallback<String>() {
                    @Override
                    public void call(String jsonResponse) {

                        // so this should be JSON content...
                        try {
                            JSONObject targetJSONResponse = new JSONObject(jsonResponse);
                            // replace content as needed
                            final String urlForWebViewAsText = targetJSONResponse.getString("bannerimage");
                            final String showingBus = targetJSONResponse.getString("offerinage");
                            final String title=targetJSONResponse.getString("title");
                            final String subtitle=targetJSONResponse.getString("subtitle");
                            if (urlForWebViewAsText.length() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        text_offer_title.setText(title);
                                        text_offer_desc.setText(subtitle);
                                        Glide.with(BusBookingActivity.this).load(urlForWebViewAsText).into(webView);
                                        Glide.with(BusBookingActivity.this).load(showingBus).into(showBus);

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Log.e("Error ", e.getMessage());
                            Snackbar.make(linearLayout, "Content from Target not valid JSON", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
                List<TargetRequest> requests = new ArrayList<>();
                requests.add(targetRequest1);
                // prep done, now retrieve content
                Snackbar.make(view, "Retrieving content from Target", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Target.retrieveLocationContent(requests, null);


                HashMap<String, String> contextData = new HashMap<String, String>();
                contextData.put("linktype", "arrowlinkclicked");
                MobileCore.trackAction("Flip Destination", contextData);

            }
        });


        findViewById(R.id.btn_find_buses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });


        findViewById(R.id.rel_offer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusBookingActivity.this, OfferDetailsActivity.class));
            }
        });

        findViewById(R.id.fragOffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusBookingActivity.this, SampleFragmentActivity.class));
            }
        });

        setSource("San Francisco");
        setDest("Las Vegas");
    }


    private void setUpToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        toolbar.setTitle("Bus Booking");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void flipSourceDesti() {

        Animation animClockWise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anin_rotating_50_clockwise);

        final Animation aniAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_rotate_50_anti_clockwise);

        aniAntiClockWise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnFlip.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animClockWise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mBtnFlip.setVisibility(View.INVISIBLE);
                mBtnFlip.startAnimation(aniAntiClockWise);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnFlip.startAnimation(animClockWise);
        startAnimation();

    }

    /**
     * Switch animation
     */
    private void startAnimation() {

        Animation forLeavingFromIn = AnimationUtils.loadAnimation(this, R.anim.left_to_right_in);
        Animation forGoingToIn = AnimationUtils.loadAnimation(this, R.anim.right_to_left_in);

        final Animation forLeavingFromOut = AnimationUtils.loadAnimation(this, R.anim.left_to_right_out);
        final Animation forGoingToOut = AnimationUtils.loadAnimation(this, R.anim.right_to_left_out);

        forLeavingFromIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateCities();
                mTextSource.startAnimation(forLeavingFromOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        forGoingToIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextDestination.startAnimation(forGoingToOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        mTextSource.startAnimation(forLeavingFromIn);
        mTextDestination.startAnimation(forGoingToIn);
    }


    /**
     * Interchange
     */
    private void updateCities() {
        String strTemp = mTextSource.getText().toString().trim();
        mTextSource.setText(mTextDestination.getText().toString().trim());
        mTextDestination.setText(strTemp);
    }


    private void setDest(String city) {
        mTextGoingTo.setVisibility(View.VISIBLE);
        mTextDestination.setText(city);
        mTextDestination.setTextColor(ContextCompat.getColor(this, R.color.black_opac));

    }


    private void setSource(String city) {

        mTextLeavingFrom.setVisibility(View.VISIBLE);
        mTextSource.setText(city);
        mTextSource.setTextColor(ContextCompat.getColor(this, R.color.black_opac));
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobileCore.setApplication(getApplication());
        MobileCore.lifecycleStart(null);
        HashMap cData = new HashMap<String, String>();
        cData.put("cd.section", "Bus Booking");
        cData.put("cd.subSection", "Booking");
        cData.put("cd.conversionType", "Landing");
        MobileCore.trackState("Test Booking Screen", cData);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showConfirmationDialog() {
        SampleDialogFragment sampleDialogFragment = SampleDialogFragment.getInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag("dialogView");
        if (prevFragment != null) {
            fragmentTransaction.remove(prevFragment);
        }
        fragmentTransaction.addToBackStack(null);
        sampleDialogFragment.show(fragmentTransaction, "dialogView");
    }

}
