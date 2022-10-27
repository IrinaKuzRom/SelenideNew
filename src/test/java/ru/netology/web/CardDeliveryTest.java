package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

   // LocalDate today = LocalDate.now();
   // LocalDate newDate = today.plusDays(3);
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);

    }

    @Test
    void shouldSendFormWithValidData() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Сидоров Иван");
        $("[data-test-id=phone] input").setValue("+79250001111");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(10000));
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldSendFormWithInvalidSurname() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Sidorov Иван");
        $("[data-test-id=phone] input").setValue("+79250001111");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendFormWithInvalidPhoneNumber() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Сидоров Иван");
        $("[data-test-id=phone] input").setValue("+792500011111");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSendFormWithInvalidCity() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Тегеран");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Сидоров Иван");
        $("[data-test-id=phone] input").setValue("+79250001111");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSendFormWithInvalidDate() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").doubleClick().sendKeys("25.10.2022");
        $("[data-test-id=name] input").setValue("Сидоров Иван");
        $("[data-test-id=phone] input").setValue("+79250001111");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithEmptyName() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79250001111");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithEmptyNumber() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Сидоров Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Вологда");
        $("[data-test-id=date] input").sendKeys(planningDate);
        $("[data-test-id=name] input").setValue("Сидоров Иван");
        $("[data-test-id=phone] input").setValue("+79250001111");
        $(".button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
