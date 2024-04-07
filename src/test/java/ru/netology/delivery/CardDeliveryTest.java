package ru.netology.delivery;


import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure"); }


    @Test
    @DisplayName("Should successful plan meeting")
    public void shouldSuccessfulPlanMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAdAddForFirstMeeting = 4;
        String fistMeetingDate = DataGenerator.generateDate(daysToAdAddForFirstMeeting);
        int daysToAdAddForSecondMeeting = 7;
        String SecondMeeting = DataGenerator.generateDate(daysToAdAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(fistMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] ").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + fistMeetingDate)).shouldBe(visible);

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(SecondMeeting);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $("[data-test-id='replan-notification'] button ").click();
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + SecondMeeting)).shouldBe(visible);
    }
}
