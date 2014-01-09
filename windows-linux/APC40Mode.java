enum APC40Mode {
    Generic, AbletonLive, AlternateAbletonLive;

    public static APC40Mode getMode(int modeNumber) {
        switch (modeNumber) {
            case 0: return Generic;
            case 1: return AbletonLive;
            case 2: return AlternateAbletonLive;
        }
        throw new IllegalArgumentException("Unsupported mode number: " + modeNumber);
    }

    public int getNumber() {
        switch (this) {
            case Generic: return 0;
            case AbletonLive: return 1;
            case AlternateAbletonLive: return 2;
        }
        throw new RuntimeException("Unsupported enum: " + this);
    }

    public byte getByte() {
        switch (this) {
            case Generic: return (byte) 0x40;
            case AbletonLive: return (byte) 0x41;
            case AlternateAbletonLive: return (byte) 0x42;
        }
        throw new RuntimeException("Unsupported enum: " + this);
    }

    @Override
    public String toString() {
        switch (this) {
            case Generic: return "Generic Mode";
            case AbletonLive: return "Ableton Live Mode";
            case AlternateAbletonLive: return "Alternate Ableton Live Mode";
        }
        throw new RuntimeException("Unsupported enum: " + this);
    }
}
