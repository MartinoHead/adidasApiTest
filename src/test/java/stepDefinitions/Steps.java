package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import apiRequests.Requests;
import org.json.simple.parser.ParseException;

public class Steps {
    Requests requests = new Requests();

    @Before
    public void before() {
        System.out.println("START");
    }

    @After
    public void after() {
        System.out.println("END OF TEST");
    }

    @Given("New Test")
    public void newTest() {
        System.out.println("New REST Assured test starts...");
    }

    @When("Get {word} pets")
    public void getPetsByStatus(String status) {
        requests.getPetsByStatus(status);
    }

    @When("Add new pet with status {word}")
    public void addNewPet(String status) throws ParseException {
        requests.postNewPet(status);
    }

    @When("Update pet status to {word}")
    public void updatePetStatus(String status) throws ParseException {
        System.out.println("Update Pet Status to " + status);
        requests.updatePetStatus(status);
    }

    @When("Delete pet")
    public void deletePetById() {
        requests.deletePetById();
    }
}
