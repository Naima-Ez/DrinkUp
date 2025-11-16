import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.drinkup.utils.ValidationResult
import com.example.drinkup.utils.ValidationUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33]) // compile-time constant, ضروري فـ Robolectric
class ValidationUtilsTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        // الحصول على سياق التطبيق
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testValidateEmail_validEmail_returnsSuccess() {
        val result = ValidationUtils.validateEmail("test@example.com", context)
        Assert.assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testValidateEmail_emptyEmail_returnsError() {
        val result = ValidationUtils.validateEmail("", context)
        Assert.assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun testValidatePassword_validPassword_returnsSuccess() {
        val result = ValidationUtils.validatePassword("password123", context)
        Assert.assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testValidatePassword_tooShort_returnsError() {
        val result = ValidationUtils.validatePassword("12345", context)
        Assert.assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun testValidateGoal_validGoal_returnsSuccess() {
        val result = ValidationUtils.validateGoal(2.5f, context)
        Assert.assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testValidateGoal_tooLarge_returnsError() {
        val result = ValidationUtils.validateGoal(15f, context)
        Assert.assertTrue(result is ValidationResult.Error)
    }
}