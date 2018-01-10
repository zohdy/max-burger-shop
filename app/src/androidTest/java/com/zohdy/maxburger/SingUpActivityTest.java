package com.zohdy.maxburger;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.activities.SignInActivity;
import com.zohdy.maxburger.activities.SignUpActivity;
import com.zohdy.maxburger.interfaces.Constants;

import org.junit.After;
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
 * Created by peterzohdy on 10/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class SingUpActivityTest {

    DatabaseReference userTable = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_TABLE_USER);


    @Rule
    public ActivityTestRule<SignUpActivity> activityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @After
    public void deleteTestUserFromDb() {
        DatabaseReference userTable = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_TABLE_USER);
        userTable.child("21304505").removeValue();
    }

    @Test
    public void TestUserAllreadyExists() {
        onView(withId(R.id.et_name)).perform(clearText()).perform(typeText("Peter Zohdy"));
        onView(withId(R.id.et_phone)).perform(clearText()).perform(typeText("26719945"));
        onView(withId(R.id.et_email)).perform(clearText()).perform(typeText("zohdy@me.com"));
        onView(withId(R.id.et_password)).perform(clearText()).perform(typeText("1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_sign_up)).perform((click()));

        // Test Toast
        onView(withText("Denne bruger er allerede registreret!")).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow()
                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestFieldMissingText() {
        onView(withId(R.id.et_phone)).perform(clearText()).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_sign_up)).perform((click()));

        // Test Toast
        onView(withText("Alle felter skal udfyldes")).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow()
                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestSuccessFullLogin() {
        onView(withId(R.id.et_name)).perform(clearText()).perform(typeText("David Jensen"));
        onView(withId(R.id.et_phone)).perform(clearText()).perform(typeText("21304505"));
        onView(withId(R.id.et_email)).perform(clearText()).perform(typeText("david@jensen.com"));
        onView(withId(R.id.et_password)).perform(clearText()).perform(typeText("1234"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_sign_up)).perform((click()));

        // Test Toast
        onView(withText("Registrering successful!")).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow()
                .getDecorView()))))
                .check(matches(isDisplayed()));

    }



}
