package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.example.Entity.Basket;
import org.example.Entity.Photo;
import org.example.Entity.User;
import org.example.TelegramBot.NasibaMCHJ;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.example.Databases.Database.*;
import static org.example.Databases.DatabaseConnection.connection;


public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi=new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new NasibaMCHJ());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }











    static{
        Type type = new TypeToken<ArrayList<Basket>>(){}.getType();
        Gson gson = new Gson();
        try {
            Statement statement = connection().createStatement();
            ResultSet executeQuery = statement.executeQuery("select * from users");
            while (executeQuery.next()) {
                userHashMap.put(String.valueOf(executeQuery.getLong(6)),new User(
                        executeQuery.getLong(1),
                        executeQuery.getString(2),
                        executeQuery.getString(3),
                        executeQuery.getString(4),
                        executeQuery.getString(5),
                        executeQuery.getString(6),
                        executeQuery.getString(7),
                        executeQuery.getString(8),
                        executeQuery.getBoolean(9),
                        executeQuery.getString(10),
                        gson.fromJson(executeQuery.getString(11),type)
                ));
            }

            statement =connection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from photo");
            while (resultSet.next()) {
                Photo photo = new Photo();
                photo.setId(resultSet.getString(1));
                photo.setFileId(resultSet.getString(2));
                photo.setCaption(resultSet.getString(3));
                photo.setProductCode(resultSet.getString(4));
                if (resultSet.getString(5).equals("pantalon")) {
                    pantalon.add(photo);
                }
                else if (resultSet.getString(5).equals("topik")){
                    topik.add(photo);
                }
                else if (resultSet.getString(5).equals("arzon")){
                    arzon.add(photo);
                }
                else if (resultSet.getString(5).equals("mens")){
                    mens.add(photo);
                }
                else if (resultSet.getString(5).equals("Maykalar")){
                    Maykalar.add(photo);
                }
                else if (resultSet.getString(5).equals("qimmat")){
                    qimmat.add(photo);
                }
                else if (resultSet.getString(5).equals("bandaj")){
                    bandaj.add(photo);
                }
                else if (resultSet.getString(5).equals("padraskovi")){
                    padraskovi.add(photo);
                }
                else if (resultSet.getString(5).equals("ugilBola")){
                    ugilBola.add(photo);
                }
                else if (resultSet.getString(5).equals("qizBolaLastichka")){
                    qizBolaLastichka.add(photo);
                }
                else if (resultSet.getString(5).equals("qizBolaShortik")){
                    qizBolaShortik.add(photo);
                }
                else if (resultSet.getString(5).equals("Merslar")){
                    Merslar.add(photo);
                }
                else if (resultSet.getString(5).equals("sales")){
                    sales.add(photo);
                }
                else if (resultSet.getString(5).equals("newGoods")){
                    newGoods.add(photo);
                }
                else if (resultSet.getString(5).equals("allWomenGoods")){
                    allWomenGoods.add(photo);
                }
            }
            ResultSet resultSet1 = statement.executeQuery("select * from basket");
            while (resultSet1.next()) {
              basketList.add( new Basket(
                resultSet1.getString(2),
                        resultSet1.getLong(3),
                        resultSet1.getString(4),
                        resultSet1.getInt(5)

                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}