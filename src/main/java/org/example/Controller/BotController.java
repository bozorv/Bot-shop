package org.example.Controller;

import org.example.Databases.Database;
import org.example.Servise.MessengerService;
import org.example.TelegramBot.NasibaMCHJ;
import org.example.Entity.User;
import org.example.Servise.ButtonServise;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.sql.Statement;


import static org.example.Button.ButtonArray.adminMarkup;
import static org.example.Button.ButtonArray.userMenu;
import static org.example.Button.ButtonName.*;
import static org.example.Databases.DatabaseConnection.connection;
import static org.example.State.UserState.*;

public class BotController {
    ButtonServise buttonServise = new ButtonServise();
    MessengerService service = new MessengerService();

    public void controller(Update update, SendMessage sendMessage) {

        String chatId = String.valueOf(update.getMessage().getChatId());
        sendMessage.setChatId(chatId);
        contact(update, sendMessage);

    }

    public void contact(Update update, SendMessage sendMessage) {
        String chatId = update.getMessage().getChatId().toString();
        sendMessage.setText("""
                Xurmatli xaridor iltimos ma'lumotlaringizni kiriting ☺️
                Ism, Familiya, Sharifingizni kiriting(Misol: Abdulloh Abulloyev Abdulloh o'g'li)
                """);
        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(update.getMessage().getFrom().getFirstName());
        user.setState(REGISTER);
        user.setExitState(NAME);
        try {

                       Statement statement = connection().createStatement();
            boolean execute = statement.execute("insert into users (id,first_name,state,exit_state,phone_number,chat_id)" +
                    " values " +
                    "("+user.getChatId()+",'" + update.getMessage().getFrom().getFirstName() + "'" +
                    ",'" + REGISTER + "'" +
                    ",'" + NAME + "'" +
                    ",'"+Math.random() * ((40000 - 10000 + 1) + 10000)+"','"+chatId+"')");


        } catch (Exception e) {
            e.printStackTrace();
        }
        Database.userHashMap.put(update.getMessage().getChatId().toString(), user);
        new NasibaMCHJ().sendMsg(sendMessage);

    }

    public void state(Update update, User user, SendMessage sendMessage) throws SQLException, ClassNotFoundException {
        switch (user.getState()) {
            case REGISTER -> buttonServise.Register(update, user, sendMessage);
            case NEW -> service.menu(update, user, sendMessage);
            case ORDER_GOODS -> {
                service.orderGoodsFirstMenu(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case CHAT_WITH_ADMIN -> {
                service.chatWithAdmin(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case BLOCKED -> {
                sendMessage.setText("🚷 Sizga ruxsat etilmagan!");
                new NasibaMCHJ().sendMsg(sendMessage);
            }

            case UGIL_BOLA -> {
                service.ugilBolaSection(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case QIZ_BOLA -> {
                service.qizBolaLatichkaSection(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case MAYKALAR -> {
                service.maykalarSection(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case SALES_GOODS -> {
                service.salesSection(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case NEW_GOODS -> {
                service.yangilikSection(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            case SAVAT -> {
                service.savatSection(update, user, sendMessage);
                new NasibaMCHJ().sendMsg(sendMessage);
            }
        }
    }

    //service.adminAddGoods(update,admin);
    public void adminstate(Update update, User admin, SendMessage sendMessage) throws SQLException, ClassNotFoundException {
        switch (admin.getState()) {
            case REGISTER -> buttonServise.Register(update, admin, sendMessage);
            case NEW -> adminController(admin, update, sendMessage);
            case ACTIVE_USERS -> buttonServise.showUserId(update, admin, sendMessage);

            case ADD_GOODS -> subcategory(update, admin, sendMessage);
            case ADD_MAYKALAR -> buttonServise.checkMaykalarPhoto(update, admin, sendMessage);
            case ADD_QIZ_BOLA -> buttonServise.checkQizBolaLastichkaPhoto(update, admin, sendMessage);
            case ADD_UGIL_BOLA -> buttonServise.checkUgilBolaPhoto(update, admin, sendMessage);
            case ADD_SALES_GOODS -> buttonServise.checkSalesGoodsPhoto(update, admin, sendMessage);
            case ADD_NEW_GOODS -> buttonServise.checkNewGoodsPhoto(update, admin, sendMessage);

            case DELETE_GOODS -> deleteSubcategory(update, admin, sendMessage);
            case DELETE_MAYKALAR -> buttonServise.deleteMaykalarPhoto(update, admin, sendMessage);
            case DELETE_UGIL_BOLA -> buttonServise.deleteUgilBolaPhoto(update, admin, sendMessage);
            case DELETE_QIZ_BOLA -> buttonServise.deleteQizBolaPhoto(update, admin, sendMessage);
            case DELETE_SALES_GOODS -> buttonServise.deleteSalesGoodsPhoto(update, admin, sendMessage);
            case DELETE_NEW_GOODS -> buttonServise.deleteNewGoodsPhoto(update, admin, sendMessage);


            default -> {
                sendMessage.setText("Bunday bo'lim yuq.");
                sendMessage.setChatId(admin.getChatId());
                sendMessage.setReplyMarkup(service.getKeyboard(adminMarkup));
                new NasibaMCHJ().sendMsg(sendMessage);
            }
        }

    }


    private void deleteSubcategory(Update update, User admin, SendMessage sendMessage) {
        String text = update.getMessage().getText();
        switch (text) {
            case DELETE_MAYKALAR -> buttonServise.deleteMaykalarPhoto(update, admin, sendMessage);
            case DELETE_UGIL_BOLA -> buttonServise.deleteUgilBolaPhoto(update, admin, sendMessage);
            case DELETE_QIZ_BOLA -> buttonServise.deleteQizBolaPhoto(update, admin, sendMessage);
            case DELETE_SALES_GOODS -> buttonServise.deleteSalesGoodsPhoto(update, admin, sendMessage);
            case DELETE_NEW_GOODS -> buttonServise.deleteNewGoodsPhoto(update, admin, sendMessage);
            case EXIT -> {
                sendMessage.setChatId(admin.getChatId());
                sendMessage.setText("Bo'limni tanlang");
                admin.setState(NEW);
                service.dbConnectionState(admin);
                sendMessage.setReplyMarkup(new MessengerService().getKeyboard(adminMarkup));
                new NasibaMCHJ().sendMsg(sendMessage);
            }
            default -> {
                sendMessage.setText("Bunday bo'lim yuq.");
                sendMessage.setChatId(admin.getChatId());
                sendMessage.setReplyMarkup(service.getKeyboard(adminMarkup));
                new NasibaMCHJ().sendMsg(sendMessage);
            }
        }
    }

    private void subcategory(Update update, User admin, SendMessage sendMessage) {
        String text = update.getMessage().getText();
        switch (text) {
            case ADD_MAYKALAR ->  buttonServise.addMaykalarPhoto(admin, sendMessage);
            case ADD_UGIL_BOLA ->  buttonServise.addUgilBolaPhoto(admin, sendMessage);
            case ADD_QIZ_BOLA ->  buttonServise.addQizBolaLastichkaPhoto(admin, sendMessage);
            case ADD_SALES_GOODS ->  buttonServise.addSalesGoodsPhoto(admin, sendMessage);
            case ADD_NEW_GOODS ->  buttonServise.addNewGoodsPhoto(admin, sendMessage);
            case EXIT -> service.subExit(admin, sendMessage);
            default -> {
                sendMessage.setText("Bunday bo'lim yuq.");
                sendMessage.setChatId(admin.getChatId());
                sendMessage.setReplyMarkup(service.getKeyboard(adminMarkup));
                new NasibaMCHJ().sendMsg(sendMessage);
            }
        }
    }


    private void adminController(User admin, Update update, SendMessage sendMessage) {
        String text = update.getMessage().getText();
        switch (text) {
            case ADD_GOODS -> {
                service.adminAddGoods(admin, sendMessage);
            }
            case DELETE_GOODS -> {
                service.adminDeleteGoods(admin, sendMessage);
            }
            case ACTIVE_USERS -> {
                service.adminAcfiveUsers(update, admin,sendMessage);
            }
            default -> {
                sendMessage.setChatId(admin.getChatId());
                sendMessage.setText("Bunday bo'lim yuq.");
                sendMessage.setReplyMarkup(service.getKeyboard(adminMarkup));
                new NasibaMCHJ().sendMsg(sendMessage);
            }

        }
    }

}
