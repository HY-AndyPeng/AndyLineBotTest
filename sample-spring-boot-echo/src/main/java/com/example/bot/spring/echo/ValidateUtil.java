package com.example.bot.spring.echo;

import java.util.Date;

public class ValidateUtil {

	public static <T> T validate(T obj) {
		return obj;
	}

	/**
	 * 驗證經銷商IvrCode
	 * 
	 * @param headerName
	 * @param value
	 * @return
	 */
	public static String validateDealerIvrCode(String value) {
		String message = null;
		if (value == null || value.trim().isEmpty()) {
			message = "經銷商IvrCode為空";
		} else {
			if (value.length() != 7) {
				message = "經銷商IvrCode長度不正確";
			}
		}
		return message;
	}

	public static String validatePromotionId(String value) {
		String message = null;
		if (value == null || value.trim().isEmpty()) {
			message = "促案OfferId為空";
		} else {
		}
		return message;
	}

	public static String validatePromotionCode(String value) {
		String message = null;
		if (value == null || value.trim().isEmpty()) {
			message = "促案代碼為空";
		} else {
		}
		return message;
	}

	public static String validatePromotionSalesTime(Date startDate, Date endDate) {
		String message = null;

		Date nowDate = new Date();
		if (startDate != null && nowDate.getTime() < startDate.getTime()) {
			// 現在時間小於起始時間
			message = "此促案還沒到銷售時間，無法銷售";
		} else if (endDate != null && nowDate.getTime() > endDate.getTime()) {
			// 現在時間大於結束時間
			message = "此促案超過銷售時間，無法銷售";
		}
		return message;
	}
}
