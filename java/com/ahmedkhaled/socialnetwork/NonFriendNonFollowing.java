package com.ahmedkhaled.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class NonFriendNonFollowing extends AppCompatActivity {

    String mainAccountUserID;
    String userID;
    String name;
    String email;
    String mobile;
    String profilePicture;
    String coverPhoto;
    String bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_friend_non_following);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userID = intent.getStringExtra("_id");
        loadUserData();
    }

    public void loadUserData()
    {
        String url = "http://178.62.119.179:5000/api/user/getuser/" + userID;

        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i("Hamada",response.toString());
                        TextView userNameTextView = findViewById(R.id.userNameTextView);
                        TextView emailTextView = findViewById(R.id.emailTextView);
                        TextView mobileTextView = findViewById(R.id.mobileTextView);
                        TextView bioTextView = findViewById(R.id.bioTextViewID);
                        ImageView profilePictureImageView = findViewById(R.id.profilePicture);
                        ImageView coverPhotoImageView = findViewById(R.id.coverPicture);
                        try
                        {
                            name = response.getJSONObject("user").getString("name");
                            if (name.equals(""))
                                userNameTextView.setText("Username");
                            else
                                userNameTextView.setText(name);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            userNameTextView.setText("Username");
                        }
                        try
                        {
                            email = response.getJSONObject("user").getString("email");
                            if (email.equals(""))
                                emailTextView.setText("Email: ");
                            else
                                emailTextView.setText("Email: " + email);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            emailTextView.setText("Email: ");
                        }
                        try
                        {
                            mobile = response.getJSONObject("user").getString("mobile");
                            if (mobile.equals(""))
                                mobileTextView.setText("Mobile: ");
                            else
                                mobileTextView.setText("Mobile: " + mobile);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            mobileTextView.setText("Mobile: ");
                        }
                        try
                        {
                            bio = response.getJSONObject("user").getString("bio");
                            if (bio.equals(""))
                                bioTextView.setText("You can enter your bio here.");
                            else
                                bioTextView.setText(bio);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            bioTextView.setText("You can enter your bio here.");
                        }
                        try
                        {
                            profilePicture = response.getJSONObject("user").getString("profileImage");
                            if (profilePicture.equals("img"))
                                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Default_profile_picture_%28male%29_on_Facebook.jpg/600px-Default_profile_picture_%28male%29_on_Facebook.jpg").into(profilePictureImageView);
                            else
                                Picasso.get().load("http://178.62.119.179:5000/" + profilePicture).into(profilePictureImageView);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Default_profile_picture_%28male%29_on_Facebook.jpg/600px-Default_profile_picture_%28male%29_on_Facebook.jpg").into(profilePictureImageView);
                        }
                        try
                        {
                            coverPhoto = response.getJSONObject("user").getString("cover");
                            if (coverPhoto.equals("cover"))
                                Picasso.get().load("https://ibridgenetwork.org/images/content/default-cover.jpg").into(coverPhotoImageView);
                            else
                                Picasso.get().load("http://178.62.119.179:5000/" + coverPhoto).into(coverPhotoImageView);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Picasso.get().load("https://ibridgenetwork.org/images/content/default-cover.jpg").into(coverPhotoImageView);
                        }
                    }
                    @Override
                    public void onError(ANError error)
                    {
                        Log.i("Hamada", error.getErrorDetail());
                        Log.i("Hamada",error.getErrorBody());
                        // handle error
                        Toast.makeText(NonFriendNonFollowing.this, "Unable to load profile", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void followUserButton(View view)
    {
        //Test Case
        mainAccountUserID = "5afa2de18024b6bbc5d01fb1";
        //Default Case
        //mainAccountUserID = getSharedPreferences("user", MODE_PRIVATE).getString("_id", "");
        AndroidNetworking.post("http://178.62.119.179:5000/api/user/follow")
                .addHeaders("content-type", "application/json")
                .addBodyParameter("myID", mainAccountUserID)
                .addBodyParameter("followingID", userID)
                .setTag("follow")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.i("Hamada",response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.i("Hamada", error.getErrorDetail());
                        Log.i("Hamada", error.getErrorBody());
                    }
                });

        Intent intent = new Intent(getApplicationContext(),MiddleMan.class);
        intent.putExtra("_id",userID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),AllMembers.class);
        startActivity(intent);
    }
}
