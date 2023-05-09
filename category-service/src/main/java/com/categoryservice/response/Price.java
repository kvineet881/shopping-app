package com.categoryservice.response;

public class Price {
	
		private String currency;
		private double amount;
		
		public Price() {
			super();
			
		}
		
		public Price(String currency, double amount) {
			super();
			this.currency = currency;
			this.amount = amount;
		}

		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}

		
}
