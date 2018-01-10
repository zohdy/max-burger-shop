package com.zohdy.maxburger;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.zohdy.maxburger.activities.HomeActivity;
import com.zohdy.maxburger.activities.SignInActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by peterzohdy on 09/01/2018.
 */

public class SingInActivityIntentTest {

    @Rule
    public IntentsTestRule<SignInActivity> intentsTestRule = new IntentsTestRule<>(SignInActivity.class);

    @Test
    public void TestSuccessFullLogin() {
        onView(withId(R.id.et_phone)).perform(clearText()).perform(typeText("26719945"));
        onView(withId(R.id.et_password)).perform(clearText()).perform(typeText("1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_sign_in)).perform((click()));

        // Test intent goes to HomeActivity
        intended(hasComponent(HomeActivity.class.getName()));
    }
}
