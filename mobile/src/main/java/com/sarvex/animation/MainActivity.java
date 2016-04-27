package com.sarvex.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

  private View container;
  private View welcome;
  private View signIn;

  private EditText username;
  private ImageView profilePicture;

  private boolean playAnimations = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    container = findViewById(R.id.container);
    welcome = findViewById(R.id.welcome);
    profilePicture = (ImageView) findViewById(R.id.profile_picture);
    signIn = findViewById(R.id.sign_in);
    username = (EditText) findViewById(R.id.username);

    username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus && username.getText().toString().equals("anna")) {
          changeProfilePicture();
        }
      }
    });
  }

  private void changeProfilePicture() {
    profilePicture.animate().rotationY(90).setDuration(750).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        profilePicture.setImageResource(R.mipmap.photo1);
        profilePicture.animate().rotationY(0).setDuration(750).setInterpolator(new OvershootInterpolator());
      }
    });
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus && playAnimations) {
      showContainer();
      playAnimations = false;
    }
  }

  private void showContainer() {
    container.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        showOtherItems();
      }
    });
  }

  private void showOtherItems() {
    float startXWelcome = 0 - welcome.getWidth();
    float endXWelcome = welcome.getX();

    ObjectAnimator welcomeAnimator = ObjectAnimator.ofFloat(welcome, View.X, startXWelcome, endXWelcome);
    welcomeAnimator.setDuration(1500);
    welcomeAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        welcome.setVisibility(View.VISIBLE);
      }
    });

    PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
    PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);

    ObjectAnimator profilePictureAnimator = ObjectAnimator.ofPropertyValuesHolder(profilePicture, scaleXHolder, scaleYHolder);
    profilePictureAnimator.setDuration(1500);

    ObjectAnimator signInAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.sign_in_animator);
    signInAnimator.setTarget(signIn);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(welcomeAnimator).after(profilePictureAnimator);
    animatorSet.play(welcomeAnimator).before(signInAnimator);
    animatorSet.start();
  }
}
