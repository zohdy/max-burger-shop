package com.zohdy.maxburger;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.zohdy.maxburger.activities.SignInActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by peterzohdy on 09/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {

    @Rule
    public ActivityTestRule<SignInActivity> activityTestRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void TestWrongPassword() {
        onView(withId(R.id.et_phone)).perform(clearText()).perform(typeText("26719945"));
        onView(withId(R.id.et_password)).perform(clearText()).perform(typeText("WrongPassword"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_sign_in)).perform((click()));

        // Test Toast
        onView(withText("Forkert Password")).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow()
                .getDecorView()))))
                .check(matches(isDisplayed()));
    }


    @Test
    public void TestWrongUserName() {
        onView(withId(R.id.et_phone)).perform(clearText()).perform(typeText("000"));
        onView(withId(R.id.et_password)).perform(clearText()).perform(typeText("1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_sign_in)).perform((click()));

        // Test Toast
        onView(withText("Brugeren findes ikke i databasen")).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow()
                .getDecorView()))))
                .check(matches(isDisplayed()));

    }
}
