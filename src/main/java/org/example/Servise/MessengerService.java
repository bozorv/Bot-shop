package org.example.Servise;

import org.example.Entity.Basket;
import org.example.Entity.User;
import org.example.TelegramBot.NasibaMCHJ;
import org.example.TelegramBot.Token;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.example.Button.ButtonArray.*;
import static org.example.Button.ButtonArray.exit;
import static org.example.Button.ButtonName.*;
import static org.example.Databases.Database.*;
import static org.example.Databases.DatabaseConnection.connection;
import static org.example.State.UserState.*;
import static org.example.TelegramBot.Token.BOT_ADMIN_ID;

public class MessengerService {


	public ReplyKeyboardMarkup getKeyboard(String[][] buttons) {
		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setSelective(true);
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboardRow = new ArrayList<>();
		for (String[] buttonList : buttons) {
			KeyboardRow row = new KeyboardRow();
			for (String s : buttonList) {
				row.add(s);
			}
			keyboardRow.add(row);
		}
		markup.setKeyboard(keyboardRow);
		return markup;
	}

	public void menu(Update update, User user, SendMessage sendMessage) {
		String text = update.getMessage().getText();
		if (text.equals(ORDER_GOODS)) {
			sendMessage.setChatId(user.getChatId());
			user.setState(ORDER_GOODS);
			dbConnectionState(user);
			sendMessage.setText("Bo'limni tanlang");
			sendMessage.setReplyMarkup(getKeyboard(userMenu));

		} else if (text.equals(CHAT_WITH_ADMIN)) {
			sendMessage.setChatId(user.getChatId());
			user.setState(CHAT_WITH_ADMIN);
			dbConnectionState(user);
			sendMessage.setText("Taklif va shikoyatlaringizni yozib qoldiring.");
			sendMessage.setReplyMarkup(getKeyboard(new String[][]{{"⬅️ Qaytish"}}));
		} else if (text.equals(SAVAT)) {
			savatSection(update, user, sendMessage);
		} else
			sendMessage.setText("Xato bo'lim");
		new NasibaMCHJ().sendMsg(sendMessage);
	}

	public void chatWithAdmin(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		if (userMessage.equals(EXIT)) {
			sendMessage.setChatId(update.getMessage().getChatId());
			user.setState(NEW);
			dbConnectionState(user);
			sendMessage.setText("Bo'limni tanlang");
			sendMessage.setReplyMarkup(getKeyboard(firstMenu));

		} else {
			SendMessage sendMessage1 = new SendMessage();
			sendMessage1.setChatId(update.getMessage().getChatId());
			sendMessage1.setText("Xabar adminga yuborildi. Admin javobini kuting");
			new NasibaMCHJ().sendMsg(sendMessage1);

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("MIJOZ nomi: ").append(user.getFirstName()).append("\n");
			stringBuilder.append("Telefon raqami: ").append(user.getContact()).append("\n");
			stringBuilder.append(userMessage);
			sendMessage.setChatId(Token.BOT_ADMIN_ID);
			sendMessage.setText(String.valueOf(stringBuilder));
			sendMessage.setReplyMarkup(inlineceboarteBuuton(user.getChatId()));
		}
	}

	private InlineKeyboardMarkup inlineceboarteBuuton(String chatId) {
		InlineKeyboardButton button = new InlineKeyboardButton("Javob yozish");
		button.setCallbackData(chatId + " javob");
		List<InlineKeyboardButton> row = List.of(button);
		return new InlineKeyboardMarkup(List.of(row));
	}

	public void orderGoodsFirstMenu(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		switch (userMessage) {
			case EXIT -> {
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(NEW);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(firstMenu));
			}
			case NEW_GOODS -> {
				new NasibaMCHJ().mazgi(update, NEW_GOODS, newGoods);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(NEW_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(NEW_GOODS);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case SALES_GOODS -> {
				new NasibaMCHJ().mazgi(update, SALES_GOODS, sales);
				user.setState(SALES_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(SALES_GOODS);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case UGIL_BOLA -> {
				new NasibaMCHJ().mazgi(update, UGIL_BOLA, ugilBola);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(UGIL_BOLA);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(UGIL_BOLA);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case QIZ_BOLA -> {
				new NasibaMCHJ().mazgi(update, QIZ_BOLA, qizBolaLastichka);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(QIZ_BOLA);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(QIZ_BOLA);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case MAYKALAR -> {
				new NasibaMCHJ().mazgi(update, MAYKALAR, Maykalar);
				user.setState(MAYKALAR);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(MAYKALAR);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case SAVAT -> {
				sendMessage.setText("Bo'limni tanlang.");
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Xatoo section");
		}

	}


	public void mensSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());

		switch (userMessage) {
			case EXIT -> {
				user.setState(ORDER_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Noto'g'ri buyruq kiritildi!");
		}
	}

	public void womenSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());
		switch (userMessage) {
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			case QIMMAT -> {
				new NasibaMCHJ().mazgi(update, QIMMAT, qimmat);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(QIMMAT);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(QIMMAT);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case ARZON -> {
				new NasibaMCHJ().mazgi(update, ARZON, arzon);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(ARZON);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(ARZON);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case PADRASKOVI -> {
				new NasibaMCHJ().mazgi(update, PADRASKOVI, padraskovi);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(PADRASKOVI);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(PADRASKOVI);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case TOPIK -> {
				new NasibaMCHJ().mazgi(update, TOPIK, topik);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(TOPIK);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(TOPIK);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case MERS -> {
				new NasibaMCHJ().mazgi(update, MERS, Merslar);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(MERS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(MERS);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case ALL_WOMEN_GOODS -> {
				new NasibaMCHJ().mazgi(update, ALL_WOMEN_GOODS, allWomenGoods);
				sendMessage.setChatId(update.getMessage().getChatId());
				user.setState(ALL_WOMEN_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang.");
				sendMessage.setText(ALL_WOMEN_GOODS);
				sendMessage.setReplyMarkup(getKeyboard(exit));
			}
			case EXIT -> {
				user.setState(ORDER_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			default -> {
				sendMessage.setText("Noto'g'ri buyruq kiritildi!");
				sendMessage.setReplyMarkup(getKeyboard(womens));
			}
		}
	}

	public void maykalarSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());
		switch (userMessage) {
			case EXIT -> {
				user.setState(ORDER_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Noto'g'ri buyruq kiritildi!");
		}
	}


	public void yangilikSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());
		switch (userMessage) {
			case EXIT -> {
				user.setState(ORDER_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Noto'g'ri buyruq kiritildi!");
		}
	}

	public void salesSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());
		switch (userMessage) {
			case EXIT -> {
				user.setState(ORDER_GOODS);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Noto'g'ri buyruq kiritildi!");
		}
	}

	public void savatSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		if (EXIT.equals(userMessage)) {
			user.setState(ORDER_GOODS);
			dbConnectionState(user);
			sendMessage.setText("Bo'limni tanlang");
			sendMessage.setReplyMarkup(getKeyboard(userMenu));
			return;
		}
		if (user.getBasketList().isEmpty()) {
			sendMessage.setText("Sizda mahsulot yo'q.");
			return;
		}

		sendMessage.setChatId(update.getMessage().getChatId());
		StringBuilder builder = new StringBuilder();
		long price = 0;
		long total = 0;
		List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
		for (Basket basket : user.getBasketList()) {
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
			button2.setText(basket.getProductCode());
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
		List<InlineKeyboardButton> row1 = new LinkedList<>();
		InlineKeyboardButton button5 = new InlineKeyboardButton();
		button5.setText(" 🚚 Buyurtma berish ");
		button5.setCallbackData("Buyurtma " + user.getChatId());
		row1.add(button5);
		rowList.add(row1);
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(rowList);
		builder.append("\n====================\n Barcha narxi: ").append(String.valueOf(total));

		sendMessage.setText(builder.toString());
		sendMessage.setReplyMarkup(inlineKeyboardMarkup);


	}


	//=================================================================
	//admin panel


	public void adminAddGoods(User admin, SendMessage sendMessage) {
		sendMessage.setChatId(admin.getChatId());
		sendMessage.setText("Bo'limni tanlang.");
		admin.setState(ADD_GOODS);
		dbConnectionState(admin);
		sendMessage.setReplyMarkup(getKeyboard(adminMenu));
		new NasibaMCHJ().sendMsg(sendMessage);
	}

	public void adminDeleteGoods(User admin, SendMessage sendMessage) {
		admin.setState(DELETE_GOODS);
		dbConnectionState(admin);
		sendMessage.setChatId(admin.getChatId());
		sendMessage.setText("Bo'limni tanlang.");
		sendMessage.setReplyMarkup(getKeyboard(adminDeleteMenu));
		new NasibaMCHJ().sendMsg(sendMessage);
	}

	public void adminAcfiveUsers(Update update, User admin, SendMessage sendMessage) {
		admin.setState(update.getMessage().getText());
		dbConnectionState(admin);
		StringBuilder builder = new StringBuilder();
		long count = 0;
		if (userHashMap.size()==1) {
			sendMessage.setChatId(admin.getChatId());
			sendMessage.setText("Mijozlar mavjud emas!!!");
			sendMessage.setReplyMarkup(getKeyboard(exitadmin));
			new NasibaMCHJ().sendMsg(sendMessage);
			return;
		}
		for (User value : userHashMap.values()) {
			if (value.getChatId().equals(BOT_ADMIN_ID)) {
				continue;
			}
			if (value.isActive()) {

				builder.append("Id: ").append(++count)
						.append("  clint: ").append(value.getFirstName()).append("    faolmi: ✅ \n");
				value.setId(count);
			} else {
				builder.append("Id: ").append(++count)
						.append("  clint: ").append(value.getFirstName()).append("    faolmi: ❌\n");
				value.setId(count);
			}
			try {

				Statement statement = connection().createStatement();
				statement.execute("update users set id = " + count + " where chat_id='" + value.getChatId() + "'");

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		sendMessage.setText(String.valueOf(builder));
		for (int i = 0; i < 2; i++) {
			sendMessage.setChatId(admin.getChatId());
			new NasibaMCHJ().sendMsg(sendMessage);
			sendMessage.setText("Clint idsini kiriting:");
			sendMessage.setReplyMarkup(getKeyboard(exitadmin));
		}


	}


	public void subExit(User admin, SendMessage sendMessage) {
		sendMessage.setChatId(admin.getChatId());
		sendMessage.setText("Bo'limni tanlang");
		admin.setState(NEW);
		dbConnectionState(admin);
		sendMessage.setReplyMarkup(getKeyboard(adminMarkup));
		new NasibaMCHJ().sendMsg(sendMessage);
	}




	public void ugilBolaSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());
		switch (userMessage) {
			case EXIT -> {
				user.setState(DETISKI);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Noto'g'ri buyruq kiritildi!");
		}
	}

	public void qizBolaLatichkaSection(Update update, User user, SendMessage sendMessage) {
		String userMessage = update.getMessage().getText();
		sendMessage.setChatId(update.getMessage().getChatId());
		switch (userMessage) {
			case EXIT -> {
				user.setState(DETISKI);
				dbConnectionState(user);
				sendMessage.setText("Bo'limni tanlang");
				sendMessage.setReplyMarkup(getKeyboard(userMenu));
			}
			case SAVAT -> {
				savatSection(update, user, sendMessage);
			}
			default -> sendMessage.setText("Noto'g'ri buyruq kiritildi!");
		}
	}


	public InlineKeyboardMarkup inlineKeyboardButton(String id) {
		InlineKeyboardButton button = new InlineKeyboardButton("O'chirish");
		button.setCallbackData(id + " ochirish");
		List<InlineKeyboardButton> row = List.of(button);
		return new InlineKeyboardMarkup(List.of(row));
	}

	public void dbConnectionState(User user) {
		try {
			Statement statement = connection().createStatement();
			statement.execute("update users set exit_state='" + user.getExitState() + "'," +
					"state = '" + user.getState() + "'" +
					" where chat_id='" + user.getChatId() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
