package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String state;
    private String exitState;
    private String contact;
    private String chatId;
    private String region;
    private String dateOfBirth;
    private boolean isActive =true;
    private String telegramUsername;
    private  List<Basket>basketList=new ArrayList<>();

    @Override
    public String toString() {
        String Active = (isActive) ? "Faol" : "Faol emas";

        return "Yangi Mijoz: " +
                "\n Ismi: " + firstName +
                "\n Telefon raqami: " + contact +
                "\n Telegram username: " + telegramUsername +
                "\n Tug'ilgan sanasi: " + dateOfBirth +
                "\n Viloyat: " + region +
                "\n Holati: " + Active;
    }
}

