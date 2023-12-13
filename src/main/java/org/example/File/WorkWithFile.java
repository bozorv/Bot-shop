package org.example.File;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Entity.Basket;
import org.example.Entity.Photo;
import org.example.Entity.User;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import static org.example.Databases.Database.*;
import static org.example.Databases.DatabaseConnection.connection;

public class WorkWithFile {
    public final String BASE_FOLDER = "src/main/resources";
    public final String USER_JSON = "USER.json";
    public final String MENS_JSON = "MENS.json";
    public final String BASKET_JSON = "basketrList.json";
    public final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public final File file = new File(BASE_FOLDER, USER_JSON);
    public final File menss = new File(BASE_FOLDER, MENS_JSON);
    public final File basket = new File(BASE_FOLDER, BASKET_JSON);

    public void readerUser() {

        Type type = new TypeToken<HashMap<String, User>>() {
        }.getType();
        Type type1 = new TypeToken<ArrayList<Photo>>() {
        }.getType();
        Type type2 = new TypeToken<ArrayList<Basket>>() {
        }.getType();
        try {
            userHashMap = gson.fromJson(new FileReader(file), type);
            mens = gson.fromJson(new FileReader(menss), type1);
            basketList = gson.fromJson(new FileReader(basket), type2);
            countPhotoId = (long) basketList.size();

//			Statement statement = connection().createStatement();
//			ResultSet resultSet = statement.executeQuery("select l.name,l.json_text from lists as l");
//			while (resultSet.next()) {
//				if (resultSet.getString(1).equals("userHashMap")) {
//					userHashMap = gson.fromJson(resultSet.getString(2), type);
//				} else if (resultSet.getString(1).equals("pantalon")) {
//					pantalon = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("topik")) {
//					topik = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("arzon")) {
//					arzon = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("mens")) {
//					mens = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("Maykalar")) {
//					Maykalar = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("qimmat")) {
//					qimmat = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("bandaj")) {
//					bandaj = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("padraskovi")) {
//					padraskovi = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("ugilBola")) {
//					ugilBola = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("qizBolaLastichka")) {
//					qizBolaLastichka = gson.fromJson(resultSet.getString(2), type1);
//				} else if (resultSet.getString(1).equals("qizBolaShortik")) {
//					qizBolaShortik = gson.fromJson(resultSet.getString(2), type1);
//				}else if (resultSet.getString(1).equals("Merslar")) {
//					Merslar = gson.fromJson(resultSet.getString(2), type1);
//				}else if (resultSet.getString(1).equals("sales")) {
//					sales = gson.fromJson(resultSet.getString(2), type1);
//				}else if (resultSet.getString(1).equals("newGoods")) {
//					newGoods = gson.fromJson(resultSet.getString(2), type1);
//				}else if (resultSet.getString(1).equals("allWomenGoods")) {
//					allWomenGoods = gson.fromJson(resultSet.getString(2), type1);
//				}else if (resultSet.getString(1).equals("basketList")) {
//					basketList = gson.fromJson(resultSet.getString(2), type2);
//				}
//			}


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeUsers() {
        try (PrintWriter writer1 = new PrintWriter(file); PrintWriter writer2 = new PrintWriter(menss);
            PrintWriter writer3 = new PrintWriter(basket)) {
            writer1.println(gson.toJson(userHashMap));
            writer2.println(gson.toJson(mens));
            writer3.println(gson.toJson(basketList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
