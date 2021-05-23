import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

/**
 * Run all unit tests with cucumber in it
 *
 * Available tags based on the User Stories:
 * Remove the tags from CucumberOption to run all tests
 *      - @User
 *      - @Group
 *      - @Chore
 * */
@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/assets/features"],
    glue = ["com.gabr.gabc.kelo.steps"],
    plugin = ["pretty"],
    strict = true
)
class RunCucumberTest