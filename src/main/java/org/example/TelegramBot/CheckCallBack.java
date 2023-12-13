package org.example.TelegramBot;

import com.google.gson.Gson;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Databases.Database;
import org.example.Entity.Basket;
import org.example.Entity.Photo;
import org.example.Entity.User;
import org.example.Servise.MessengerService;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.example.Button.ButtonArray.adminMarkup;
import static org.example.Button.ButtonArray.firstMenu;
import static org.example.Button.ButtonName.ANSWER;
import static org.example.Databases.Database.*;
import static org.example.Databases.DatabaseConnection.connection;
import static org.example.State.UserState.*;
import static org.example.TelegramBot.Token.BOT_ADMIN_ID;

public class CheckCallBack {
	Gson gson = new Gson();

	public boolean callback(Update update) {
		SendMessage sendMessage = new SendMessage();
		CallbackQuery callbackQuery = update.getCallbackQuery();
		String adminId = String.valueOf(callbackQuery.getMessage().getChatId());
		User admin = userHashMap.get(adminId);

		Message message = callbackQuery.getMessage();
		String data = callbackQuery.getData();
		String[] s = data.split(" ");
		if (s.length == 2) {
			if (s[1].equals("ochirish")) {
				String id = s[0];
				try {
					Statement statement2 = connection().createStatement();
					ResultSet resultSet = statement2.executeQuery("select category_name from photo where id = " + id);
					while (resultSet.next()) {
						Database.deleteMethod(resultSet.getString(1), id);
					}
					Statement statement = connection().createStatement();
					Statement statement1 = connection().createStatement();
					statement.execute("delete from photo where id = " + id);
					statement1.execute("delete from basket where photo_id = " + id);
					sendMessage.setText("O'chirildi");
					sendMessage.setChatId(admin.getChatId());
					DeleteMessage deleteMessage = new DeleteMessage(admin.getChatId(), message.getMessageId());
					new NasibaMCHJ().sendMsg(sendMessage);
					new NasibaMCHJ().sendMsg(deleteMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}


	public void has_calbac(User admin, Update update, SendMessage sendMessage, MessengerService messengerService) {

		try {
			CallbackQuery callbackQuery = update.getCallbackQuery();
			Message message = callbackQuery.getMessage();
			String data = callbackQuery.getData();
			String[] s = data.split(" ");
			if (s[1].equals("javob")) {
				admin.setExitState(admin.getState());
				Statement statement = Objects.requireNonNull(connection()).createStatement();
				statement.execute("update users set exit_state='" + admin.getState() + "',state ='" + ANSWER + "'" +
						" where chat_id='" + admin.getChatId() + "'");
				admin.setState(ANSWER);
				messengerService.dbConnectionState(admin);
				sendMessage.setChatId(BOT_ADMIN_ID);
				sendMessage.setText("Xabarni kiriting");
				NasibaMCHJ.userChatId = s[0];
				new NasibaMCHJ().sendMsg(sendMessage);
				return;
			}
			User user = userHashMap.get(String.valueOf(message.getChatId()));

			EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
			if (s[0].equals("plus")) {
				int count = Integer.parseInt(s[2]) + 50;
				editMessageReplyMarkup.setChatId(message.getChatId());
				editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) new NasibaMCHJ().userButton(s[1], count));
				editMessageReplyMarkup.setMessageId(message.getMessageId());
				for (Basket basket : basketList) {
					if (Objects.equals(basket.getProductCode(), s[1])) {
						Statement statement = connection().createStatement();
						for (Basket basket1 : user.getBasketList()) {
							if (basket1.getProductCode().equals(basket.getProductCode())) {
								basket1.setCount(count);
								String json = gson.toJson(user.getBasketList());
								connection().createStatement().execute("update users set basket_list = '" + json + "'::text  where chat_id='" + user.getChatId() + "'");
								new NasibaMCHJ().sendMsg(editMessageReplyMarkup);
								return;
							}
						}
						basket.setCount(count);
						user.getBasketList().add(basket);
						break;
					}
				}
				new NasibaMCHJ().sendMsg(editMessageReplyMarkup);

			} else if (s[0].equals("minus")) {
				int count = Integer.parseInt(s[2]) - 50;
				editMessageReplyMarkup.setMessageId(message.getMessageId());
				editMessageReplyMarkup.setChatId(message.getChatId());
				if (count > 0) {
					editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) new NasibaMCHJ().userButton(s[1], count));
					for (Basket basket : basketList) {
						if (Objects.equals(basket.getProductCode(), s[1])) {
							for (Basket basket1 : user.getBasketList()) {
								if (basket1.getProductCode().equals(basket.getProductCode())) {
									basket1.setCount(count);
									String json = gson.toJson(user.getBasketList());
									connection().createStatement().execute("update users set basket_list = '" + json + "'::text  where chat_id='" + user.getChatId() + "'");
									new NasibaMCHJ().sendMsg(editMessageReplyMarkup);
									return;
								}
							}
							basket.setCount(count);
							user.getBasketList().add(basket);
							break;
						}
					}
				} else {
					editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) new NasibaMCHJ().userButton(s[1], 0));
				}
				editMessageReplyMarkup.setMessageId(message.getMessageId());
				new NasibaMCHJ().sendMsg(editMessageReplyMarkup);

			} else if (s[0].equals("plu")) {
				basketCalbak(user, message, s);

			} else if (s[0].equals("minu")) {
				basketCalbak(user, message, s);

			} else if (s[0].equals("dele")) {
				basketCalbak(user, message, s);

			} else if (s[0].equals("Buyurtma")) {
				String uuid = String.valueOf(UUID.randomUUID());
				User client = userHashMap.get(s[1]);
				File file = new File("src/main/resources/Byurtmalar", uuid + ".xlsx");
				try (FileOutputStream out = new FileOutputStream(file);
				     XSSFWorkbook workbook = new XSSFWorkbook()) {
					XSSFSheet sheet = workbook.createSheet(client.getFirstName());
					XSSFRow row = sheet.createRow(0);
					row.createCell(0).setCellValue(client.getFirstName());
					row.createCell(1).setCellValue(client.getContact());
					row.createCell(2).setCellValue(client.getRegion());
					row = sheet.createRow(1);
					row.createCell(0).setCellValue("Code");
					row.createCell(1).setCellValue("Razmer");
					row.createCell(2).setCellValue("Soni");
					row.createCell(3).setCellValue("Narxi");
					row.createCell(4).setCellValue("Jami narxi");
					int count = 2;
					long total = 0;
					StringBuilder string = new StringBuilder();
					for (Basket basket : client.getBasketList()) {
						row = sheet.createRow(count++);
						row.createCell(0).setCellValue(basket.getProductCode());
						row.createCell(1).setCellValue("    ");
						row.createCell(2).setCellValue(basket.getCount());
						row.createCell(3).setCellValue(basket.getPrice());
						row.createCell(4).setCellValue((basket.getCount() * basket.getPrice()));
						string.append(" Maxsulot code: ").append(basket.getProductCode())
								.append("\n soni: ").append(basket.getCount())
								.append("\n maxsulot narxi : ").append(basket.getPrice())
								.append("\n jami narx: ").append(basket.getCount() * basket.getPrice())
								.append("\n------------------------\n");
						total += (basket.getCount() * basket.getPrice());
					}
					connection().createStatement().execute("update users set basket_list = '[]'::text  where chat_id='" + client.getChatId() + "'");
					row = sheet.createRow((count++) + 1);
					row.createCell(0).setCellValue("Barcha mahsulot narxi = ");
					row.createCell(4).setCellValue(total);
					for (int i = 0; i < 5; i++) {
						sheet.autoSizeColumn(i);
					}

					workbook.write(out);
					SendDocument document = new SendDocument(BOT_ADMIN_ID, new InputFile(file));
					document.setCaption(client.getFirstName() + " buyurtma keldi.\n" + client.getContact());
					client.getBasketList().clear();
					DeleteMessage deleteMessage = new DeleteMessage(client.getChatId(), message.getMessageId());
					string.append("Barcha maxsulot narxi: ").append(total);
					sendMessage.setText("Buyurtmangiz qabul qilindi.🚚\n" + string);
					sendMessage.setChatId(client.getChatId());
					connection().createStatement().execute("insert into orders (user_id, byurtma_code) VALUES (" + client.getChatId() + ",'" + uuid + "')");
					new NasibaMCHJ().sendMsg(deleteMessage);
					new NasibaMCHJ().sendMsg(document);
					new NasibaMCHJ().sendMsg(sendMessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (s[1].equals("ruxsat")) {
				User user1 = Database.userHashMap.get(s[0]);
				user1.setState(NEW);
				user1.setActive(true);
				Statement statement = connection().createStatement();
				statement.execute("update users set exit_state='" + user1.getExitState() + "'," +
						"state = '" + user1.getState() + "',is_active = true" +
						" where chat_id='" + user1.getChatId() + "'");
				sendMessage.setChatId(user1.getChatId());
				sendMessage.setText("Bo'limni tanlang.");
				if (user1.getChatId().equals(BOT_ADMIN_ID)) {
					sendMessage.setReplyMarkup(messengerService.getKeyboard(adminMarkup));
				} else {
					sendMessage.setReplyMarkup(messengerService.getKeyboard(firstMenu));
				}
				DeleteMessage deleteMessage = new DeleteMessage(BOT_ADMIN_ID, message.getMessageId());
				new NasibaMCHJ().sendMsg(deleteMessage);
				new NasibaMCHJ().sendMsg(sendMessage);
			} else if (s[1].equals("radEtish")) {
				User user2 = Database.userHashMap.get(s[0]);
				user2.setState(BLOCKED);
				Statement statement = connection().createStatement();
				statement.execute("update users set exit_state='" + user2.getExitState() + "'," +
						"state = '" + user2.getState() + "',is_active = false" +
						" where chat_id='" + user2.getChatId() + "'");
				sendMessage.setText("🚷 So'rovingiz rad etildi.");
				DeleteMessage deleteMessage = new DeleteMessage(BOT_ADMIN_ID, message.getMessageId());
				new NasibaMCHJ().sendMsg(deleteMessage);
			} else if (s[1].equals("userBuyurma")) {

				User client = userHashMap.get(s[0]);

				ResultSet resultSet = connection().createStatement().executeQuery("select byurtma_code from orders where user_id =" + s[0] + " ");
				while (resultSet.next()) {
					File file=new File("src/main/resources/Byurtmalar",resultSet.getString(1)+".xlsx");
					SendDocument document=new SendDocument(BOT_ADMIN_ID,new InputFile(file));
                    document.setCaption(" Eski buyurtma \n"+client.getFirstName()+"  nomeri = "+client.getContact());
                    new NasibaMCHJ().sendMsg(document);
				}
				System.out.println("Chiqarildi.");

			} else if (s[1].equals("userActive")) {
				User client = userHashMap.get(s[0]);
				client.setActive(true);
				client.setState(NEW);
				Statement statement = connection().createStatement();
				statement.execute("update users set exit_state='" + client.getExitState() + "'," +
						"state = '" + client.getState() + "'" +
						",is_active = true  where chat_id='" + client.getChatId() + "'");
				sendMessage.setText("Clint faollashtrildi.");
				sendMessage.setChatId(BOT_ADMIN_ID);
				SendMessage sendMessage1 = new SendMessage();
				sendMessage1.setChatId(client.getChatId());
				sendMessage1.setText("Bo'limni tanlang");
				sendMessage1.setReplyMarkup(messengerService.getKeyboard(firstMenu));
				new NasibaMCHJ().sendMsg(sendMessage1);
				new NasibaMCHJ().sendMsg(sendMessage);
			} else if (s[1].equals("userBloc")) {
				User client = userHashMap.get(s[0]);
				client.setActive(false);
				client.setState(BLOCKED);
				Statement statement = connection().createStatement();
				statement.execute("update users set exit_state='" + client.getExitState() + "'," +
						"state = '" + client.getState() + "'" +
						",is_active = false  where chat_id='" + client.getChatId() + "'");
				sendMessage.setText("Clint bloklandi.");
				sendMessage.setChatId(BOT_ADMIN_ID);
				new NasibaMCHJ().sendMsg(sendMessage);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void basketCalbak(User user, Message message, String[] s) {
		EditMessageText editMessageText = new EditMessageText();
		editMessageText.setMessageId(message.getMessageId());
		editMessageText.setChatId(user.getChatId());
		if (user.getBasketList().isEmpty()) {
			editMessageText.setText("Sizda mahsulot yo'q.");
			new NasibaMCHJ().sendMsg(editMessageText);
			return;
		}
		if (s[0].equals("dele") || s[0].equals("minu")) {
			for (Basket basket : user.getBasketList()) {
				if (basket.getProductCode().equals(s[1])) {
					if (basket.getCount() == 0) {
						user.getBasketList().remove(basket);
						break;
					}
					if (s[0].equals("dele")) {
						user.getBasketList().remove(basket);
						break;
					}
				}
			}
		}

		StringBuilder builder = new StringBuilder();
		long price = 0;
		long total = 0;
		List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
		for (Basket basket : user.getBasketList()) {
			if (basket.getProductCode().equals(s[1])) {
				if (s[0].equals("plu")) {
					basket.setCount(basket.getCount() + 50);
				} else if (s[0].equals("minu")) {
					basket.setCount(basket.getCount() - 50);
				}
			}
			try {
				String json = gson.toJson(user.getBasketList());
				connection().createStatement().execute("update users set basket_list = '" + json + "'::text  where chat_id='" + user.getChatId() + "'");

			} catch (SQLException e) {
				e.printStackTrace();
			}
			List<InlineKeyboardButton> row1 = new LinkedList<>();
			price = basket.getCount() * basket.getPrice();
			total += price;
			builder.append(" Code: ").append(basket.getProductCode())
					.append("\n Soni: ").append(basket.getCount())
					.append("\n Narxi: ").append(basket.getPrice())
					.append("\n Jami narxi: ").append(price).append("\n_______________\n");
			InlineKeyboardButton button1 = new InlineKeyboardButton();
			button1.setText(" ➕ 50 ");
			button1.setCallbackData("plu " + basket.getProductCode() + " " + price);
			InlineKeyboardButton button2 = new InlineKeyboardButton();
			button2.setText(" Code:" + basket.getProductCode() + " \n" + basket.getCount());
			button2.setCallbackData(basket.getProductCode() + " " + price);
			InlineKeyboardButton button3 = new InlineKeyboardButton();
			button3.setText(" ➖ 50 ");
			button3.setCallbackData("minu " + basket.getProductCode() + " " + price);
			InlineKeyboardButton button4 = new InlineKeyboardButton();
			button4.setText(" ❌ ");
			button4.setCallbackData("dele " + basket.getProductCode() + " " + price);
			row1.add(button1);
			row1.add(button2);
			row1.add(button3);
			row1.add(button4);
			rowList.add(row1);
		}
		if (user.getBasketList().isEmpty()) {
			editMessageText.setText("Sizda mahsulot yo'q.");
			new NasibaMCHJ().sendMsg(editMessageText);
			return;
		}
		List<InlineKeyboardButton> row1 = new LinkedList<>();
		InlineKeyboardButton button5 = new InlineKeyboardButton();
		button5.setText(" 🚚 Buyurtma berish ");
		button5.setCallbackData("Buyurtma " + user.getChatId());
		row1.add(button5);
		rowList.add(row1);
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(rowList);
		builder.append("\n====================\n Barcha narxi: ").append(total);
		editMessageText.setText(String.valueOf(builder));
		editMessageText.setReplyMarkup(inlineKeyboardMarkup);
		new NasibaMCHJ().sendMsg(editMessageText);
	}

	public void deleteMethod(List<Photo> mens, Message message, SendMessage sendMessage, Update update, String data) {
		String[] s = data.split(" ");
		if (s.length == 2) {
			for (Photo photo : mens) {
				if (photo.getId().equals(s[0])) {
					mens.remove(photo);
					sendMessage.setChatId(update.getCallbackQuery().getFrom().getId());
					sendMessage.setText(photo.getProductCode() + " -> O'chirildi");
					DeleteMessage deleteMessage = new DeleteMessage(BOT_ADMIN_ID, message.getMessageId());
					new NasibaMCHJ().sendMsg(deleteMessage);
					new NasibaMCHJ().sendMsg(sendMessage);
					return;
				}
			}
		}
	}
}
