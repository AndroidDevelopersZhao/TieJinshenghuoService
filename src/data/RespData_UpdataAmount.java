package data;

public class RespData_UpdataAmount {
		private int code = -1;
		private String data = null;
		
		public RespData_UpdataAmount(int code, String data) {
			super();
			this.code = code;
			this.data = data;
		}
		public int getCode() {
			return code;
		}
		public String getData() {
			return data;
		}
		
		public void setCode(int code) {
			this.code = code;
		}
		public void setData(String data) {
			this.data = data;
		}
		
}
