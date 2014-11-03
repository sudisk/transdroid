package org.transdroid.daemon.Qbittorrent;

public enum SpeedUnit implements IUnit {
	BYTE_PER_SECOND(1) {
		@Override
		public String key() {
			return "size.bytes_per_second";
		}
	},
	KIBIBYTE_PER_SECOND(1024L) {
		@Override
		public String key() {
			return "size.kibibytes_per_second";
		}
	},
	MEBIBYTE_PER_SECOND(1024L*1024L) {
		@Override
		public String key() {
			return "size.mebibytes_per_second";
		}
	},
	GIBIBYTE_PER_SECOND(1024L*1024L*1024L) {
		@Override
		public String key() {
			return "size.gibibytes_per_second";
		}
	},
	TEBIBYTE_PER_SECOND(1024L*1024L*1024L*1024L) {
		@Override
		public String key() {
			return "size.tebibytes_per_second";
		}
	};
	
	public abstract String key();

	private long multiplier;
	
	public long getMultiplier() {
		return multiplier;
	}
	
	private SpeedUnit(long multipleValue) {
		multiplier = multipleValue;
	}
}
