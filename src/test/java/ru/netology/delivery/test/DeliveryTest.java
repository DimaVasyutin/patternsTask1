package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(3);
        var secondMeetingDate = DataGenerator.generateDate(7);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), firstMeetingDate);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $(".notification__title").shouldHave(Condition.text("Успешно")).shouldBe(Condition.visible);
        $("div .notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(Condition.visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $x("//*[contains(text(), 'Запланировать')]").click();
        $x("//*[@class='button__content']//*[contains(text(), 'Перепланировать')]").click();
        $("div .notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldDateTwentyNineLeapYear() {
        LocalDate today = LocalDate.now();
        for (int i = 1; i < 4; i++) {
            if (today.isLeapYear() == true) {
                break;

            }
            today = today.plusYears(i);
        }
        String date = "29." + "02." + today.getYear();
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Успешно")).shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + date))
                .shouldBe(Condition.visible);

    }

    @Test
    void shouldValidDataYoNameCity() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue("Орёл");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Успешно")).shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + date))
                .shouldBe(Condition.visible);

    }

    @Test
    void shouldCityNotAdminCenter() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue("Вязьма");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"), visible);

    }

    @Test
    void shouldCityNameLatin() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(DataGenerator.generateCity("us"));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"), visible);

    }

    @Test
    void shouldCityNameSymbols() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id=city] input").setValue("!!!!!");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"), visible);

    }

    @Test
    void shouldCityNameNumbers() {
        String date = DataGenerator.generateDate(7);
        Faker faker = new Faker();
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(String.valueOf(faker.number().randomDigit()));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"), visible);

    }

    @Test
    void shouldCityNameEmpty() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldCityNameSpaces() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue("    ");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldCityNameTab() {
        String date = DataGenerator.generateDate(7);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue("       ");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=city] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldDateLessThanThreeDays() {
        String date = DataGenerator.generateDate(1);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"), visible);

    }

    @Test
    void shouldDateInThePast() {
        String date = DataGenerator.generateDate(-2);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"), visible);

    }

    @Test
    void shouldDateNoExist() {
        LocalDate today = LocalDate.now();
        String date = "35" + "12" + today.getYear();
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldDateTwentyNineNoLeapYear() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        LocalDate today = LocalDate.now();
        if (today.isLeapYear()) {
            today = today.plusYears(1);
        }
        String date = "29" + "02" + today.getYear();

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldDateSymbol() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("!!!;!");
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldDateLetters() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("кукук");
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldDateEmpty() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("");
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldDateSpaces() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("     ");
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldDateTab() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("       ");
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=date] span .input__sub").shouldHave(Condition.text("Неверно введена дата"), visible);

    }

    @Test
    void shouldNameLatin() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(DataGenerator.generateName("us"));
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name] span .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), visible);

    }

    @Test
    void shouldNameSymbol() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue("%^^^^");
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name] span .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), visible);

    }

    @Test
    void shouldNameNumbers() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue("855244");
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name] span .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), visible);

    }

    @Test
    void shouldNameSpaces() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue("     ");
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldNameTab() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue("         ");
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldNameEmpty() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue("");
        $("[name=phone]").setValue(validUser.getPhone());
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=name] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldPhoneLetters() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("аааdssdsd");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), visible);

    }

    @Test
    void shouldPhoneBeginInsteadPlusSevenAnotherNumber() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");
        Faker faker = new Faker();

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("+" + String.valueOf(faker.number().randomDigit()) + "9055554444");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), visible);

    }

    @Test
    void shouldPhoneSymbol() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("!!!!!!!!&&&");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), visible);

    }

    @Test
    void shouldPhoneLessElevenNumber() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("+79999");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), visible);

    }

    @Test
    void shouldPhoneSpaces() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("    ");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldPhoneTab() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("            ");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldPhoneEmpty() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue("");
        $("[data-test-id=agreement] span").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=phone] span .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"), visible);

    }

    @Test
    void shouldEmptyCheckBox() {
        String date = DataGenerator.generateDate(3);
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        assertEquals($("[data-test-id=date] input").getValue(), date);
        $("[name=name]").setValue(validUser.getName());
        $("[name=phone]").setValue(validUser.getPhone());
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("label.input_invalid").shouldHave(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"), visible);

    }
}
