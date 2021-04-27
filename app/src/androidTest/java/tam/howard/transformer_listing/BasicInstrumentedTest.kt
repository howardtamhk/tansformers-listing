package tam.howard.transformer_listing

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hamcrest.Matchers.anyOf
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tam.howard.transformer_listing.ui.splash.SplashActivity
import tam.howard.transformer_listing.utils.BaseRobot

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BasicInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<SplashActivity> =
        ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun fight(){
        addTransformerAutobot()
        addTransformerDeception()
        BaseRobot().doOnView(withId(R.id.button_listing_fight), click())
        GlobalScope.launch {
            delay(1000)
            BaseRobot().doOnView(withId(android.R.id.button1), click())
        }
    }

    private fun addTransformerAutobot() {
        BaseRobot().doOnView(withId(R.id.menu_item_listing_add), click())
        BaseRobot().doOnView(withId(R.id.edit_text_edit_name), typeText("AutoBot"))
        BaseRobot().doOnView(withId(R.id.button_edit_team_autobot), click())
        BaseRobot().doOnView(withId(R.id.button_edit_save), click())

        onView(anyOf(withText("AutoBot"))).check(matches(isDisplayed()))
    }

    private fun addTransformerDeception() {
        BaseRobot().doOnView(withId(R.id.menu_item_listing_add), click())
        BaseRobot().doOnView(withId(R.id.edit_text_edit_name), typeText("Deception"))
        BaseRobot().doOnView(withId(R.id.button_edit_team_decepticon), click())
        BaseRobot().doOnView(withId(R.id.button_edit_save), click())


        BaseRobot().assertOnView(anyOf(withText("Deception")), matches(isDisplayed()))
    }

    companion object{
        @BeforeClass
        @JvmStatic
        fun setUp() {
            InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
                BuildConfig.APPLICATION_ID,
                Context.MODE_PRIVATE
            ).edit().clear().commit()
        }
    }
}