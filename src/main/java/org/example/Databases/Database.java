package org.example.Databases;

import org.example.Entity.Basket;
import org.example.Entity.Photo;
import org.example.Entity.User;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.example.Databases.DatabaseConnection.connection;


public class Database {

	public static HashMap<String, User> userHashMap = new HashMap<>();
	public static List<Photo> pantalon = new ArrayList<>();
	public static List<Photo> topik = new ArrayList<>();
	public static List<Photo> arzon = new ArrayList<>();
	public static List<Photo> mens = new ArrayList<>();
	public static List<Photo> Maykalar = new ArrayList<>();
	public static List<Photo> qimmat = new ArrayList<>();
	public static List<Photo> bandaj = new ArrayList<>();
	public static List<Photo> padraskovi = new ArrayList<>();
	public static List<Photo> ugilBola = new ArrayList<>();
	public static List<Photo> qizBolaLastichka = new ArrayList<>();
	public static List<Photo> qizBolaShortik = new ArrayList<>();
	public static List<Photo> Merslar = new ArrayList<>();
	public static List<Photo> sales = new ArrayList<>();
	public static List<Photo> newGoods = new ArrayList<>();
	public static List<Photo> allWomenGoods = new ArrayList<>();
	public static List<Basket> basketList = new ArrayList<>();
	public static Long countPhotoId;






	public static String photoCountId(){
		try {
			Statement statement = connection().createStatement();
			ResultSet resultSet = statement.executeQuery("select id from photo order by id desc limit 1");
			while (resultSet.next()) {
				countPhotoId = (long) resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (countPhotoId!=null) {
			countPhotoId++;
		}else {
			countPhotoId=(long)0;
			countPhotoId++;
		}
		return String.valueOf(countPhotoId);
	}



	public static void deleteMethod (String name, String photoId){
		switch (name) {
			case "pantalon" -> {
				for (Photo men : pantalon) {
					if (men.getId().equals(photoId)) {
						pantalon.remove(men);
						break;
					}
				}
			}
			case "topik" -> {
				for (Photo men : topik) {
					if (men.getId().equals(photoId)) {
						topik.remove(men);
						break;
					}
				}
			}
			case "arzon" -> {
				for (Photo men : arzon) {
					if (men.getId().equals(photoId)) {
						arzon.remove(men);
						break;
					}
				}
			}
			case "mens" ->{
				for (Photo men : mens) {
					if (men.getId().equals(photoId)) {
						mens.remove(men);
						break;
					}
				}
			}
			case "Maykalar" -> {
				for (Photo men : Maykalar) {
					if (men.getId().equals(photoId)) {
						Maykalar.remove(men);
						break;
					}
				}
			}
			case "qimmat" ->{
				for (Photo men : qimmat) {
					if (men.getId().equals(photoId)) {
						qimmat.remove(men);
						break;
					}
				}
			}
			case "bandaj" ->{
				for (Photo men : bandaj) {
					if (men.getId().equals(photoId)) {
						bandaj.remove(men);
						break;
					}
				}
			}
			case "padraskovi" ->{
				for (Photo men : padraskovi) {
					if (men.getId().equals(photoId)) {
						padraskovi.remove(men);
						break;
					}
				}
			}
			case "ugilBola" ->{
				for (Photo men : ugilBola) {
					if (men.getId().equals(photoId)) {
						ugilBola.remove(men);
						break;
					}
				}
			}
			case "qizBolaLastichka" ->{
				for (Photo men : qizBolaLastichka) {
					if (men.getId().equals(photoId)) {
						qizBolaLastichka.remove(men);
						break;
					}
				}
			}
			case "qizBolaShortik" ->{
				for (Photo men : qizBolaShortik) {
					if (men.getId().equals(photoId)) {
						qizBolaShortik.remove(men);
						break;
					}
				}
			}
			case "Merslar" ->{
				for (Photo men : Merslar) {
					if (men.getId().equals(photoId)) {
						Merslar.remove(men);
						break;
					}
				}
			}
			case "sales" ->{
				for (Photo men : sales) {
					if (men.getId().equals(photoId)) {
						sales.remove(men);
						break;
					}
				}
			}
			case "newGoods" ->{
				for (Photo men : newGoods) {
					if (men.getId().equals(photoId)) {
						newGoods.remove(men);
						break;
					}
				}
			}
			case "allWomenGoods" ->{
				for (Photo men : allWomenGoods) {
					if (men.getId().equals(photoId)) {
						allWomenGoods.remove(men);
						break;
					}
				}
			}

		}
		for (Basket basket : basketList) {
			if (basket.getPhotoId().equals(photoId)) {
				basketList.remove(basket);
				return;
			}
		}
	}

}













