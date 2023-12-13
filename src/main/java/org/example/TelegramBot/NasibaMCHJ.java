package org.example.TelegramBot;

import lombok.SneakyThrows;
import org.example.Controller.BotController;
import org.example.Databases.Database;
import org.example.Entity.Basket;
import org.example.Entity.Photo;
import org.example.Entity.Product;
import org.example.Entity.User;
import org.example.File.WorkWithFile;
import org.example.Servise.MessengerService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.example.Button.ButtonArray.blocUser;
import static org.example.Button.ButtonName.ANSWER;
import static org.example.Button.ButtonName.CHAT_WITH_ADMIN;
import static org.example.Databases.Database.*;
import static org.example.TelegramBot.Token.*;

public class NasibaMCHJ extends TelegramLongPollingBot {
	List<Product> products = new LinkedList<>();
	CheckCallBack checkCallBack = new CheckCallBack();
	MessengerService messengerService = new MessengerService();
	BotController botController = new BotController();
	WorkWithFile workWithFile = new WorkWithFile();
	public static String userChatId = "";

	@Override
	public String getBotUsername() {
		return BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	@SneakyThrows
	@Override
	public void onUpdateReceived(Update update) {

		try {


		if (!update.hasCallbackQuery()){
			if (update.getMessage().hasAudio() ||
					update.getMessage().hasVoice() ||
					update.getMessage().hasSticker() ||
					update.getMessage().hasAnimation() ||
					update.getMessage().hasContact() ||
					update.getMessage().hasVideo() ||
					update.getMessage().hasDice() ||
					update.getMessage().hasVideoNote()) {
				sendMsg(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Xato Malumot"));
				return;
			} else if (update.getMessage().hasPhoto() && !String.valueOf(update.getMessage().getChatId()).equals(BOT_ADMIN_ID)) {
				sendMsg(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Xato Malumot"));
				return;
			}
		}


		User admin = userHashMap.get(BOT_ADMIN_ID);
		SendMessage sendMessage = new SendMessage();
		if (admin != null&&update.hasMessage()) {
			if (Objects.equals(admin.getChatId(),String.valueOf(update.getMessage().getChatId()))&&admin.getState().equals(ANSWER)) {
				admin.setState(admin.getExitState());
				sendMessage.setChatId(userChatId);
				sendMessage.setText(" Admin javobi \n " + update.getMessage().getText());
				SendMessage sendMessage1 =new SendMessage(BOT_ADMIN_ID,"Xabar jo'natildi.");
				sendMsg(sendMessage);
				sendMsg(sendMessage1);
				return;
			}
		}

		if (update.hasCallbackQuery()) {
			if (checkCallBack.callback(update)) {
				return;
			}
			checkCallBack.has_calbac(admin, update, sendMessage, messengerService);
			return;
		}


		if (update.getMessage().hasPhoto()) {
			botController.adminstate(update, admin, sendMessage);
		} else {

			sendMessage.setChatId(update.getMessage().getChatId());
			if (!Database.userHashMap.containsKey(String.valueOf(update.getMessage().getChatId()))) {
				botController.controller(update, sendMessage);
			} else {
				User user = Database.userHashMap.get(String.valueOf(update.getMessage().getChatId()));
				if (user != null) {
					if ((user.getChatId().equals(BOT_ADMIN_ID)) && update.getMessage().hasText() && user.isActive()) {
						botController.adminstate(update, user, sendMessage);
					} else {
						if (user.isActive()) {
							botController.state(update, user, sendMessage);
						} else {
								sendMessage.setChatId(user.getChatId());
								sendMessage.setText(" Admin bilan bog'laning @"+admin.getTelegramUsername()+"\n   "+admin.getContact());
//								sendMessage.setReplyMarkup(messengerService.getKeyboard(blocUser));
								execute(sendMessage);
						}
					}
				} else {
					System.out.println("User null");
				}
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void adminDeleteGoods(Update update, String type, List<Photo> mahsulot) {
		try {
			String text=update.getMessage().getText();;

			if (mahsulot.isEmpty()) {
					execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Masulot mavjud emas!"));

				return;
			}

			if (text != null && Objects.equals(text, type)) {
				for (Photo photo : mahsulot) {
						execute(SendPhoto.builder()
								.chatId(update.getMessage().getChatId())
								.photo(new InputFile(photo.getFileId()))
								.replyMarkup(messengerService.inlineKeyboardButton(photo.getId()))
								.caption(photo.getCaption())
								.build());

				}
			}
		}catch (Exception e){
			e.printStackTrace();
			try {
				execute(new SendMessage(String.valueOf(update.getMessage().getChatId()),"Masulot mavjud emas!"));
			} catch (TelegramApiException ex) {
				e.printStackTrace();
			}
		}

	}

	public void mazgi(Update update, String type, List<Photo> listName) {
		User user = userHashMap.get(String.valueOf(update.getMessage().getChatId()));
		String text;
		if (update.getMessage().getText() == null) {
			text = " ";
		} else {
			text = update.getMessage().getText();
		}

		if (text != null && Objects.equals(text, type)) {
			for (Photo photo : listName) {
				try {
					int count = 0;
					for (Basket basket : user.getBasketList()) {
						if (photo.getProductCode().equals(basket.getProductCode())) {
							count = basket.getCount();
							break;
						}
					}
					execute(SendPhoto.builder()
							.chatId(update.getMessage().getChatId())
							.photo(new InputFile(photo.getFileId()))
							.replyMarkup(userButton(photo.getProductCode(), count))
							.caption(photo.getCaption())
							.build());
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendMsg(Object object) {
		try {
			if (object instanceof SendMessage sendMessage) {
				execute(sendMessage);
			} else if (object instanceof DeleteMessage deleteMessage) {
				execute(deleteMessage);
			} else if (object instanceof EditMessageReplyMarkup edit) {
				execute(edit);
			} else if (object instanceof EditMessageText editText) {
				execute(editText);
			} else if (object instanceof SendDocument document) {
				execute(document);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public ReplyKeyboard userButton(String id, int count) {
		List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
		List<InlineKeyboardButton> row1 = new LinkedList<>();
		InlineKeyboardButton button1 = new InlineKeyboardButton();
		button1.setText(" ➕ ");
		button1.setCallbackData("plus " + id + " " + count);
		InlineKeyboardButton button2 = new InlineKeyboardButton();
		button2.setText(String.valueOf(count));
		button2.setCallbackData("delete " + id + " " + count);

		InlineKeyboardButton button3 = new InlineKeyboardButton();
		button3.setText(" ➖ ");
		button3.setCallbackData("minus " + id + " " + count);

		row1.add(button1);
		row1.add(button2);
		row1.add(button3);
		rowList.add(row1);
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(rowList);
		return inlineKeyboardMarkup;

	}


}
