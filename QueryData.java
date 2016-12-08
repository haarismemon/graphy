public class QueryData{
		
		public QueryData(String indicator, String country, int[] dates) {
			
			this.indicator = indicator;
			this.country = country;
			this.dates = dates;
		}
		
		private String indicator = "";
		private String country;
		private int[] dates;
		
		public int[] getDates() {
			return dates;
		}
		public void setDates(int[] dates) {
			this.dates = dates;
		}
		public String getIndicator() {
			return indicator;
		}
		public void setIndicator(String indicator) {
			this.indicator = indicator;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}

	}
